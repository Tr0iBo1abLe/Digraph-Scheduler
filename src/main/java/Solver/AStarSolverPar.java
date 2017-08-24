package Solver;

import Datastructure.FastPriorityBlockingQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

public final class AStarSolverPar extends AbstractSolver {
    private final Queue<SearchState> queue;
    private Timer timer;

    public AStarSolverPar(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
        queue = new FastPriorityBlockingQueue<>();
    }

    @Override
    public void doSolve() {
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

        queue.add(currBestState);
        while (true) {
            currBestState = queue.remove();
            if (currBestState.getNumVertices() == graph.getVertices().size()) {
                // We have found THE optimal solution
                if (updater != null && timer != null) {
                    updater.update(currBestState);
                    timer.cancel();
                }
                return;
            }
            currBestState.getLegalVertices().parallelStream().forEach(vertex -> IntStream.range(0, processorCount).parallel().forEach(processor -> {
                SearchState next = new SearchState(currBestState, vertex, processor);
                if (!queue.contains(next)) {
                    queue.add(next);
                }
            }));
        }
    }
}
