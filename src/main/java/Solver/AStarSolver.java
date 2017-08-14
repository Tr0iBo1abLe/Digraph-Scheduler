package Solver;

import Datastructure.FastPriorityQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

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
        SearchState.init(graph);

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
            SearchState searchState = queue.remove();

            if (searchState.getSize() == graph.getVertices().size()) {
                // We have found THE optimal solution
                scheduleVertices(searchState);
                if (updater != null && timer != null) {
                    updater.update(searchState);
                    timer.cancel();
                }
                return;
            }

            for (Vertex vertex : searchState.getLegalVertices()) {
                for (int processorID = 0; processorID < processorCount; processorID++) {
                    SearchState nextSearchState = new SearchState(searchState, vertex, processorID);
                    if (!queue.contains(nextSearchState)) {
                        queue.add(nextSearchState);
                    }
                }
            }
        }
    }

    /*
    OPEN ← emptyState
    while OPEN 6 = ∅ do s ← PopHead ( OPEN )
    if s is complete solution then return s as optimal solution
    Expand state s into children and compute f ( s child )
     for each OPEN ← new states
     */
}
