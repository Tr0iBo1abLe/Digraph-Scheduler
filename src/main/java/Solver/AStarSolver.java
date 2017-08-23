package Solver;

import Datastructure.FastPriorityQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

public final class AStarSolver extends AbstractSolver {

    private final Queue<SearchState> queue;
    private Timer timer;

    public AStarSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
        queue = new FastPriorityQueue<>();
    }

    @Override
    public void doSolve() {
        /* This method is blocking, we need a way to notify the GUI */
        SearchState.initialise(graph);

        if (updater != null) {
            /* We have an updater and a UI to update */
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                                          @Override
                                          public void run() {
                                              updater.update(queue.peek());
                                          }
                                      },
                    100, 100);
        }

        queue.add(new SearchState());
        while (true) {
            SearchState currBestState = queue.remove();

            if (currBestState.getNumVertices() == graph.getVertices().size()) {
                // We have found THE optimal solution
                scheduleVertices(currBestState);
                if (updater != null && timer != null) {
                    updater.update(currBestState);
                    timer.cancel();
                }
                return;
            }

            currBestState.getLegalVertices().forEach(vertex -> IntStream.range(0, processorCount).forEach(processor -> {
                SearchState nextSearchState = new SearchState(currBestState, vertex, processor);
                if (!queue.contains(nextSearchState)) {
                    queue.add(nextSearchState);
                }
            }));

        }
    }
}
