package Solver;

import Solver.Interfaces.ISolver;
import lombok.Builder;
import lombok.Data;
import org.graphstream.graph.Graph;

import java.util.*;
import java.util.stream.IntStream;

public class AStarSolver extends AbstractSolver{

    public AStarSolver(Graph graph, int processorCount) {
        super(graph, processorCount);
    }

    @Override
    public void doSolve() {
        SearchState.init(graph);
        System.out.println(graph.getNodeCount());

        Queue<SearchState> queue = new PriorityQueue<>();
        Set<SearchState> set = new HashSet<>();
        queue.add(new SearchState());

        while(true) {
            SearchState s = queue.remove();
            set.remove(s);
            if(s.getSize() == graph.getNodeCount()) {
                // We have found THE optimal solution
                scheduleVertices(s);
                return;
            }
            s.getLegalVertices().forEach( v -> {
                IntStream.of(0, processorCount-1).forEach(i -> {
                            SearchState next = new SearchState(s, v, i);
                            if(!set.contains(next)) {
                                set.add(next);
                                queue.add(next);
                            }
                        }
                );
            });
        }

    }
}
