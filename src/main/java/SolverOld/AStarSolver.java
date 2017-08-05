package SolverOld;

import Datastructure.FastPriorityQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;

import java.util.*;
import java.util.stream.IntStream;

public final class AStarSolver extends AbstractSolver{

    public AStarSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
    }

    @Override
    public void doSolve() {
        SearchState.init(graph);

        Queue<SearchState> queue = new FastPriorityQueue<>();
        queue.add(new SearchState());

        while(true) {

            SearchState s = queue.remove();
            if(s.getSize() == graph.getVertices().size()) {
                // We have found THE optimal solution
                scheduleVertices(s);
                return;
            }
            s.getLegalVertices().forEach( v -> {
                IntStream.of(0, processorCount-1).forEach( i -> {
                            SearchState next = new SearchState(s, v, i);
                            if(!queue.contains(next)) {
                                queue.add(next);
                            }
                        }
                );
            });
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
