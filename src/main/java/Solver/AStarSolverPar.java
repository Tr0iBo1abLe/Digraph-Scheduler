package Solver;

import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import lombok.Data;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Data
public final class AStarSolverPar extends AbstractSolver {
    public AStarSolverPar(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
    }

    @Override
    public void doSolve() {

        Queue<SearchState> queue = new PriorityBlockingQueue<>();
        Queue<SearchState> resQueue = new PriorityBlockingQueue<>();
        Set<SearchState> set = Collections.newSetFromMap(new ConcurrentHashMap<SearchState, Boolean>());
        queue.add(new SearchState(graph));

        while(true) {
            SearchState s = queue.remove();
            set.remove(s);
            if(s.getSize() == graph.getVertices().size()) {
                // We have found THE optimal solution
                scheduleVertices(s);
                return;
            }
            s.getLegalVertices().parallelStream().forEach( v -> {
                IntStream.of(0, processorCount-1).parallel().forEach( i -> {
                            SearchState next = new SearchState(s, v, i);
                            if(!set.contains(next)) {
                                set.add(next);
                                queue.add(next);
                            }
                        }
                );
            });
            /* Expansion */
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
