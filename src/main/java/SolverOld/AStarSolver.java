package SolverOld;

import CommonInterface.ISearchState;
import Datastructure.FastPriorityQueue;
import GUI.IUpdatableState;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;

import java.util.*;
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
        SearchState.init(graph);

        if(updater != null) {
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

        while(true) {
            SearchState s = queue.remove();
            //updater.update(s);
            //System.err.println(s.getSize() + " " + s.getPriority() + " " + queue.size());
            if(s.getSize() == graph.getVertices().size()) {
                // We have found THE optimal solution
                scheduleVertices(s);
                if(updater != null && timer != null) {
                    updater.update(s);
                    timer.cancel();
                }
                return;
            }
            s.getLegalVertices().forEach( v -> {
                IntStream.range(0, processorCount).forEach( i -> {
                            SearchState next = new SearchState(s, v, i);
                            if(!queue.contains(next)) {
                                queue.add(next);
                            }
                        }
                );
            });
        }
    }

    //@Override
    public ISearchState pollState() {
        return queue.peek();
    }

    /*
    OPEN ← emptyState
    while OPEN 6 = ∅ do s ← PopHead ( OPEN )
    if s is complete solution then return s as optimal solution
    Expand state s into children and compute f ( s child )
     for each OPEN ← new states
     */
}
