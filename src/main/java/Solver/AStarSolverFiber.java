package Solver;

import Datastructure.FastPriorityBlockingQueue;
import Datastructure.FastPriorityQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Util.Helper;
import co.paralleluniverse.common.util.SystemProperties;
import co.paralleluniverse.fibers.*;
import javafx.application.Platform;
import lombok.extern.log4j.Log4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

/**
 * A*, uses BFS and a priority queue to ensure the first schedule found is optimal.
 * Uses a lot of memory and will switch to the DFS Solver by passing over the current best state if the queue gets too big.
 * <p>
 *
 * @author Dovahkiin Huang, Will Molloy
 */
@Log4j
public final class AStarSolverFiber extends AbstractSolver {

    private final Queue<SearchState> queue;
    private Timer guiTimer = timer;

    public AStarSolverFiber(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
        queue = new FastPriorityBlockingQueue<>();
    }

    @Override
    void doSolve() {
        Properties props = System.getProperties();
        props.setProperty("co.paralleluniverse.fibers.verifyInstrumentation", "false");
        /* This method is blocking, we need a way to notify the GUI */
        if (updater != null) {
            /* We have an updater and a UI to update */
            guiTimer = new Timer();
            guiTimer.scheduleAtFixedRate(new TimerTask() {
                                             @Override
                                             public void run() {
                                                 Platform.runLater(() -> updater.update(queue.peek(), AStarSolverFiber.this));
                                             }
                                         },
                    100, 100);
        }

        queue.add(currBestState);
        while (Helper.getRemainingMemory() > 600_000_000L) { // GB, MB, kB
            currBestState = queue.remove();

            if (currBestState.getNumVertices() == graph.getVertices().size()) {
                // We have found THE optimal solution
                if (updater != null && guiTimer != null) {
                    Platform.runLater(() -> updater.update(currBestState, AStarSolverFiber.this));
                    guiTimer.cancel();
                }
                return;
            }

            Set<Fiber<Void>> fiberSet = makeFibres(currBestState);
            fiberSet.forEach(Fiber::start);
            Set<SearchState> searchStateSetTemp = Collections.newSetFromMap(new ConcurrentHashMap<>());
            fiberSet.forEach(fiber -> {
                try {
                    fiber.join();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            queue.addAll(searchStateSetTemp);
        }
        continueSolveWithBnB();
    }

    private Set<Fiber<Void>> makeFibres(SearchState searchState) {
        Set<Vertex> legalVertices = searchState.getLegalVertices();
        Set<Fiber<Void>> fiberSet = new LinkedHashSet<>();
        legalVertices.forEach(vertex -> {
            fiberSet.add(new Fiber<Void>() {
                @Override
                protected Void run() throws SuspendExecution, InterruptedException {
                    IntStream.range(0, processorCount).forEach(processor -> {
                        SearchState searchState1 = new SearchState(currBestState, vertex, processor);
                        if(!queue.contains(searchState1)) queue.add(searchState1);
                    });
                    return null;
                }
            });
        });
        return fiberSet;
    }


    private void continueSolveWithBnB() {
        if (guiTimer != null) guiTimer.cancel();
        log.debug("Calling DFSSolver");

        // transfer the current optimal state and clear the rest.
        DFSSolver dfsSolver = new DFSSolver(getGraph(), getProcessorCount(), currBestState);
        queue.clear();
        dfsSolver.setUpdater(getUpdater());
        System.gc();

        dfsSolver.completeSolve();
    }
}
