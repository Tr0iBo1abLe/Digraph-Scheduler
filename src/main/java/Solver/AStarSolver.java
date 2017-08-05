package Solver;

import Datastructure.FastPriorityQueue;
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

        Queue<SearchState> queue = new FastPriorityQueue<>();
        queue.add(new SearchState());

        while(true) {
            SearchState s = queue.remove();
            if(s.getSize() == graph.getNodeCount()) {
                // We have found THE optimal solution
                scheduleVertices(s);
                return;
            }
            s.getLegalVertices().forEach( v -> {
                IntStream.of(0, processorCount-1).forEach(i -> {
                            SearchState next = new SearchState(s, v, i);
                            if(!queue.contains(next)) {
                                queue.add(next);
                            }
                        }
                );
            });
        }

    }
}
