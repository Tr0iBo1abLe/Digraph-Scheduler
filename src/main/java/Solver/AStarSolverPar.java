package Solver;

import CommonInterface.ISearchState;
import Datastructure.FastPriorityBlockingQueue;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.graphstream.graph.Graph;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public final class AStarSolverPar extends AbstractSolver {
    private final Queue<SearchState> queue;
    public AStarSolverPar(Graph graph, int processorCount) {
        super(graph, processorCount);
        queue = new FastPriorityBlockingQueue<>();
    }

    @Override
    public void doSolve() {
        SearchState.init(graph);
        if(updater != null) {
            /* We have an updater and a UI to update */
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                                          @Override
                                          public void run() {
                                              updater.update(queue.peek());
                                          }
                                      },
                    200, 200);
        }
        queue.add(new SearchState());

        while(true) {
            SearchState s = queue.remove();
            if(s.getSize() == graph.getNodeCount()) {
                // We have found THE optimal solution
                scheduleVertices(s);
                return;
            }
            s.getLegalVertices().parallelStream().forEach( v -> {
                IntStream.of(0, processorCount-1).parallel().forEach( i -> {
                            SearchState next = new SearchState(s, v, i);
                            if(!queue.contains(next)) {
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
