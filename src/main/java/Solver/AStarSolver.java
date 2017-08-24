package Solver;

import Datastructure.FastPriorityQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import javafx.application.Platform;
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

    public AStarSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
        queue = new FastPriorityQueue<>();
    }

    @Override
    void doSolve() {
        if (updater != null) {
            /* We have an updater and a UI to update */
            isUpdatableProgressBar = true;
            AbstractSolver solver = this; //provide a reference to GUI classes
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                                          @Override
                                          public void run() {
                                              Platform.runLater(()->updater.update(queue.peek(), solver));
                                          }
                                      },
                    100, 100);
        }

        queue.add(currBestState);
        while (Helper.getRemainingMemory() > 600_000_000L) { // GB, MB, kB
            currBestState = queue.remove();

            if (currBestState.getNumVertices() == graph.getVertices().size()) {
                // We have found THE optimal solution
                timer.cancel();
                if (updater != null && timer != null) {
                    Platform.runLater(() -> updater.update(currBestState, this));
                }
                return;
            }

            currBestState.getLegalVertices().forEach(vertex -> IntStream.range(0, processorCount).forEach(processor -> {
                SearchState nextSearchState = new SearchState(currBestState, vertex, processor);
                if (!queue.contains(nextSearchState)) {
                    queue.add(nextSearchState);
                    //increase the state counter for GUI, process only when there is a GUI to update
                    if (isUpdatableProgressBar){ //true if there is a GUI progress bar needs to be updated
                        stateCounter++;
                    }
                }
            }));
        }
        continueSolveWithBnB();
    }

    private void continueSolveWithBnB() {
        if (timer != null) timer.cancel();
        log.debug("Calling DFSSolver");

        // transfer the current optimal state and clear the rest.
        DFSSolver dfsSolver = new DFSSolver(getGraph(), getProcessorCount(), currBestState);
        queue.clear();
        dfsSolver.setUpdater(getUpdater());
        System.gc();

        dfsSolver.completeSolve();
    }
}
