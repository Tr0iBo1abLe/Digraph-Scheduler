package Solver;

import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Log4j
public final class DFSSolverParallel extends AbstractSolver {

    private int currUpperBound;
    private static final int parallelCount = Runtime.getRuntime().availableProcessors();
    private ThreadPoolExecutor executorService;
    private Set<Callable<Void>> callables;

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public DFSSolverParallel(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
        log.debug("Solver inited");
        log.info("Parallel processors: " + parallelCount);
        /* Constructuor to allow tests, remove when no longer needed.*/
    }

    public DFSSolverParallel(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount, int parallelCount) {
        super(graph, processorCount);
        log.debug("Solver inited");
    }

    DFSSolverParallel(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount, int parallelCount, SearchState existingState) {
        super(graph, processorCount);
        currBestState = existingState;
        log.debug("Solver inited with an existing state");
    }

    public DFSSolverParallel(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount, SearchState currBestState) {
        super(graph, processorCount);
        this.currBestState = currBestState;
        log.info("Parallel processors: " + parallelCount);
    }

    /**
     * Used when transferring state from a AStarSolver
     */
    SearchState completeSolve() {
        // The upper bound is now the currBestState + that of scheduling the remaining vertices to the same processor.
        // If there is an edge pointing to these vertices we can assume the 'same' processor is the optimal one
        currUpperBound = currBestState.getUnderestimate();
        solving(currBestState);
        return currBestState;
    }

    @Override
    void doSolve() {
        executorService = new ThreadPoolExecutor(parallelCount, parallelCount, 99999999L, TimeUnit.DAYS, new LinkedBlockingQueue<>());
        currUpperBound = Integer.MAX_VALUE;
        SearchState searchState = new SearchState();
        callables = Collections.newSetFromMap(new ConcurrentHashMap<>());
        callables.addAll(makeCallables(searchState));
        log.info("Callables count " + callables.size());
        try {
            executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //executorService.shutdown();
        while(executorService.getActiveCount() != 0);
    }

    @Synchronized
    private Set<Callable<Void>> makeCallables(SearchState searchState) {
        //log.info("Making callables");
        Set<Vertex> legalVertices = searchState.getLegalVertices();
        Set<Callable<Void>> callables = new LinkedHashSet<>();
        legalVertices.forEach(vertex -> {
            callables.add(() -> {
                IntStream.range(0, processorCount).forEach(processor -> {
                    SearchState nextState = new SearchState(searchState, vertex, processor);
                    if(checkAndUpdate(nextState)) {
                        solving(nextState);
                        atomicInteger.decrementAndGet();
                    }
                });
                return null;
            });
        });
        atomicInteger.set(atomicInteger.get() + callables.size());
        //log.info("Callable state -> " + callables.toString());
        return callables;
    }

    private void solving(SearchState currState) {
        if(atomicInteger.get() < parallelCount) {
            final Set<Callable<Void>> callables = makeCallables(currState);
            callables.forEach(callable -> {
                //atomicInteger.incrementAndGet();
                executorService.submit(callable);
            });
            return;
        }
        currState.getLegalVertices().forEach(vertex -> IntStream.range(0, processorCount).forEach(processor -> {
            SearchState nextState = new SearchState(currState, vertex, processor);
            if (checkAndUpdate(nextState)) solving(nextState);
        }));
    }

    private boolean checkAndUpdate(SearchState nextState) {
        if (nextState.getUnderestimate() >= currUpperBound) {
            return false;
        }
        if (nextState.getNumVertices() == graph.getVertices().size()) {
            updateLog(nextState);
            return false;
        }
        return true;
    }

    @Synchronized
    private void updateLog(SearchState s) {
        int underestimate = s.getUnderestimate();
        if (underestimate < currUpperBound) {
            currUpperBound = underestimate;
            currBestState = s;
        }
    }
}
