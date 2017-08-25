package Solver;

import Datastructure.FastPriorityBlockingQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import javafx.application.Platform;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

public final class AStarSolverPar extends AbstractSolver {

    private final Queue<SearchState> queue;

    public AStarSolverPar(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
        queue = new FastPriorityBlockingQueue<>();
    }

    @Override
    public void doSolve() {
        if (updater != null) {
            /* We have an updater and a UI to update */
            isUpdatableProgressBar = true;
            AbstractSolver solver = this; //provide a reference to GUI classes
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                                          @Override
                                          public void run() {
                                              Platform.runLater(()->updater.update(queue.peek(), solver)); // required by FX framework
                                          }
                                      },
                    100, 100);
        }

        queue.add(currBestState);
        while (true) {
            currBestState = queue.remove();
            if (currBestState.getNumVertices() == graph.getVertices().size()) {
                // We have found THE optimal solution
                if (updater != null && timer != null) {
                    Platform.runLater(() -> updater.update(currBestState, this)); // required by FX framework
                    timer.cancel();
                }
                return;
            }

            currBestState.getLegalVertices().parallelStream().forEach(vertex -> IntStream.range(0, processorCount).parallel().forEach(processor -> {
                SearchState next = new SearchState(currBestState, vertex, processor);
                if (!queue.contains(next)) {
                    queue.add(next);
                }
                //increase the state counter for GUI, process only when there is a GUI to update
                if (isUpdatableProgressBar){ //true if there is a GUI progress bar needs to be updated
                    stateCounter++;
                }
            }));
        }
    }
}
