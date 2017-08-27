package Solver;

import Datastructure.FastPriorityBlockingQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Util.Helper;
import javafx.application.Platform;
import lombok.extern.log4j.Log4j;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * A*, uses BFS and a priority queue to ensure the first schedule found is optimal.
 * Uses a lot of memory and will switch to the DFS Solver by passing over the current best state if the queue gets too big.
 * <p>
 *
 * @author Dovahkiin Huang, Will Molloy
 */
@Log4j
public final class AStarSolverParallelJavaExecutor extends AbstractSolver {

    private final Queue<SearchState> queue;
    private Timer guiTimer = timer;

    public AStarSolverParallelJavaExecutor(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount, int parallelProcessorCount) {
        super(graph, processorCount, parallelProcessorCount);
        queue = new FastPriorityBlockingQueue<>();
    }

    @Override
    void doSolve() {
        /* This method is blocking, we need a way to notify the GUI */
        if (updater != null) {
            /* We have an updater and a UI to update */
            guiTimer = new Timer();
            guiTimer.scheduleAtFixedRate(new TimerTask() {
                                             @Override
                                             public void run() {
                                                 Platform.runLater(() -> updater.update(queue.peek(), AStarSolverParallelJavaExecutor.this));
                                             }
                                         },
                    100, 100);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(parallelProcessorCount);

        queue.add(currBestState);
        while (Helper.getRemainingMemory() > 600_000_000L) { // GB, MB, kB
            currBestState = queue.remove();

            if (currBestState.getNumVertices() == graph.getVertices().size()) {
                // We have found THE optimal solution
                if (updater != null && guiTimer != null) {
                    Platform.runLater(() -> updater.update(currBestState, AStarSolverParallelJavaExecutor.this));
                    guiTimer.cancel();
                }
                return;
            }

            Set<Callable<Void>> callables = makeCallables(currBestState);
            try {
                executorService.invokeAll(callables);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        continueSolveWithBnB();
    }

    private Set<Callable<Void>> makeCallables(SearchState searchState) {
        Set<Vertex> legalVertices = searchState.getLegalVertices();
        Set<Callable<Void>> callables = new LinkedHashSet<>();
        legalVertices.forEach(vertex -> {
            callables.add(() -> {
                IntStream.range(0, processorCount).forEach(processor -> {
                    SearchState searchState1 = new SearchState(currBestState, vertex, processor);
                    if (!queue.contains(searchState1)) queue.add(searchState1);
                });
                return null;
            });
        });
        return callables;
    }

    private void continueSolveWithBnB() {
        if (guiTimer != null) guiTimer.cancel();
        log.debug("Calling DFSSolver");

        // transfer the current optimal state and clear the rest.
        DFSSolverParallel dfsSolver = new DFSSolverParallel(graph, processorCount, parallelProcessorCount, currBestState);
        queue.clear();
        dfsSolver.setUpdater(getUpdater());
        System.gc();

        dfsSolver.completeSolve();
    }
}
