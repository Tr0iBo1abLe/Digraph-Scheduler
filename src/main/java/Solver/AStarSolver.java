package Solver;

import CommonInterface.ISearchState;
import Datastructure.FastPriorityBlockingQueue;
import Datastructure.FastPriorityQueue;
import lombok.Data;
import org.graphstream.graph.Graph;

import java.util.*;
import java.util.stream.IntStream;

@Data
public final class AStarSolver extends AbstractSolver{

    private final Queue<SearchState> queue;
    private Timer timer;

    public AStarSolver(Graph graph, int processorCount) {
        super(graph, processorCount);
        queue = new FastPriorityQueue<>();
    }

    @Override
    public void doSolve() {
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
                    500, 500);
        }

        queue.add(new SearchState());
        while(true) {
            SearchState s = queue.remove();
            System.err.println(s.getSize() + " " + s.getPriority() + " " + queue.size());
            if(s.getSize() == graph.getNodeCount()) {
                // We have found THE optimal solution
                if(updater != null && timer != null) {
                    updater.update(s);
                    timer.cancel();
                }
                scheduleVertices(s);
                return;
            }
            s.getLegalVertices().forEach( v -> {
                IntStream.range(0, processorCount).forEach(i -> {
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
