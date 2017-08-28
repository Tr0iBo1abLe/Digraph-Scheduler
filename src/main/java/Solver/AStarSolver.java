package Solver;

import Datastructure.FastPriorityQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Util.Helper;
import javafx.application.Platform;
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
                                              Platform.runLater(() -> updater.update(queue.peek(), solver)); // required by FX framework
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
                    //increase the state counter for GUI, process only when there is a GUI to update
                    if (isUpdatableProgressBar) { //true if there is a GUI progress bar needs to be updated
                        stateCounter++;
                    }
                }
            }));

            if (currBestState.getNumVertices() == graph.getVertices().size()) {
                // We have found THE optimal solution
                if (updater != null && timer != null) {
                    Platform.runLater(() -> updater.update(currBestState, this)); // required by FX framework
                    timer.cancel();
                }
                log.info("Final queue size: " + queue.size());
                return;
            }

        } while (Helper.getRemainingMemory() > 600_000_000L); // GB, MB, kB
        continueSolveWithBnB();
    }

    private void continueSolveWithBnB() {
        if (timer != null) timer.cancel();
        log.info("Calling DFSSolver. Queue size: " + queue.size() + " State size: " + currBestState.getNumVertices());

        // transfer the current optimal state and clear the rest.
        DFSSolver dfsSolver = new DFSSolver(graph, processorCount, currBestState);
        queue.clear();
        dfsSolver.setUpdater(getUpdater());
        System.gc();

        currBestState = dfsSolver.completeSolve();
    }
}
