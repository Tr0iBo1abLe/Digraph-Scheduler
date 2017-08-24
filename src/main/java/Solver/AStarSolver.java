package Solver;

import Datastructure.FastPriorityQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Util.Helper;
import lombok.extern.log4j.Log4j;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

/**
 * A*, uses BFS and a priority queue to ensure the first schedule found is optimal.
 * Uses a lot of memory and will switch to the DFS Solver by passing over the current best state if the queue gets too big.
 * <p>
 *
 * @author Dovahkiin Huang, Will Molloy
 */
@Log4j
public final class AStarSolver extends AbstractSolver {

    private final Queue<SearchState> queue;
    private Timer guiTimer;

    public AStarSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
        queue = new FastPriorityQueue<>();
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
                                                 updater.update(queue.peek());
                                             }
                                         },
                    100, 100);
        }

        queue.add(currBestState);
        do {
            currBestState = queue.remove();

            currBestState.getLegalVertices().forEach(vertex -> IntStream.range(0, processorCount).forEach(processor -> {
                SearchState nextSearchState = new SearchState(currBestState, vertex, processor);
                if (!queue.contains(nextSearchState)) {
                    queue.add(nextSearchState);
                }
            }));

            if (currBestState.getNumVertices() == graph.getVertices().size()) {
                // We have found THE optimal solution
                if (updater != null && guiTimer != null) {
                    updater.update(currBestState);
                    guiTimer.cancel();
                }
                return;
            }
        } while (Helper.getRemainingMemory() > 600_000_000L); // GB, MB, kB
        continueSolveWithBnB();
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
