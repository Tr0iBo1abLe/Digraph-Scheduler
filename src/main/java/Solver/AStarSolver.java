package Solver;

import CommonInterface.ISearchState;
import Datastructure.FastPriorityBlockingQueue;
import Datastructure.FastPriorityQueue;
import lombok.Data;
import org.graphstream.graph.Graph;

import java.util.*;
import java.util.stream.IntStream;

@Data
public class AStarSolver extends AbstractSolver{

    private final Queue<SearchState> queue;

    public AStarSolver(Graph graph, int processorCount) {
        super(graph, processorCount);
        queue = new FastPriorityQueue<>();
    }

    @Override
    public void doSolve() {
        SearchState.init(graph);
        queue.add(new SearchState());
        while(true) {
            SearchState s = queue.remove();
            System.err.println(s.getSize() + " " + s.getPriority() + " " + queue.size());
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

    @Override
    public ISearchState pollState() {
        return queue.peek();
    }
}
