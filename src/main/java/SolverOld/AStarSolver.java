package SolverOld;

import CommonInterface.ISearchState;
import Datastructure.FastPriorityQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;

import java.util.*;
import java.util.stream.IntStream;

public final class AStarSolver extends AbstractSolver{

    private final Queue<SearchState> queue;

    public AStarSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
        queue = new FastPriorityQueue<>();
    }

    @Override
    public void doSolve() {
        SearchState.init(graph);

        queue.add(new SearchState());

        while(true) {
            SearchState s = queue.remove();
            //System.err.println(queue.size());
            //Arrays.stream(s.getProcessors()).forEach(System.err::println);
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

    @Override
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
