package Solver;

import CommonInterface.ISearchState;
import Datastructure.FastPriorityBlockingQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import lombok.Data;
import lombok.Getter;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

@Data
public final class AStarSolverPar extends AbstractSolver {
    private final Queue<SearchState> queue;
    private Timer timer;

    public AStarSolverPar(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
        queue = new FastPriorityBlockingQueue<>();
    }

    @Override
    public void doSolve() {
        SearchState.initialise(graph);


        if (updater != null) {
            /* We have an updater and a UI to update */
            isUpdatableProgressBar = true;
            AbstractSolver solver = this; //provide a reference to GUI classes
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                                          @Override
                                          public void run() {
                                              updater.update(queue.peek(), solver);
                                          }
                                      },
                    100, 100);
        }

        queue.add(new SearchState());

        while (true) {
            SearchState s = queue.remove();
            if (s.getNumVertices() == graph.getVertices().size()) {
                // We have found THE optimal solution
                if (updater != null && timer != null) {
                    updater.update(s,this);
                    timer.cancel();
                }
                scheduleVertices(s);
                return;
            }
            s.getLegalVertices().parallelStream().forEach(v -> IntStream.range(0, processorCount).parallel().forEach(i -> {
                SearchState next = new SearchState(s, v, i);
                if (!queue.contains(next)) {
                    queue.add(next);
                }
                //increase the state counter for GUI, process only when there is a GUI to update
                if (isUpdatableProgressBar){ //true if there is a GUI progress bar needs to be updated
                    stateCounter++;
                }
                    }
            ));
            /* Expansion */
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
