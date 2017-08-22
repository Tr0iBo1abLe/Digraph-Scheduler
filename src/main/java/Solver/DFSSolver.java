package Solver;

import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import lombok.Data;
import lombok.Getter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;


/**
 * Created by mason on 31/07/17.
 * @Author mason shi
 */
@Data
public final class DFSSolver extends AbstractSolver {

    private Timer timer;

    private int currentUpperBound;
    private SearchState result = new SearchState();

    public DFSSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
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
                                              updater.update(result, solver);
                                          }
                                      },
                    100, 100);
        }

        // ideally initial upperbound would be topological sort length
        currentUpperBound = graph.getVertices().stream().filter(o -> o.getCost() > 0).mapToInt(Vertex::getCost).sum();
        currentUpperBound += graph.getForwardEdges().stream().filter(o -> o.getCost() > 0).mapToInt(EdgeWithCost::getCost).sum();
        SearchState s = new SearchState();
        solving(s);
        scheduleVertices(result);
        if (updater != null && timer != null) {
            updater.update(result, this);
            timer.cancel();
        }
    }

    private void solving(SearchState s) {
        s.getLegalVertices().forEach(v -> IntStream.range(0, processorCount).forEach(i -> {
                    SearchState next = new SearchState(s, v, i);
                    if (next.getUnderestimate() >= currentUpperBound) {
                        return;
                    }
                    if (next.getNumVertices() == graph.getVertices().size()) {
                        updateLog(next);
                        return;
                    }
                    //increase the state counter for GUI, process only when there is a GUI to update
                    if (isUpdatableProgressBar){ //true if there is a GUI progress bar needs to be updated
                        stateCounter++;
                    }
                    solving(next);
                }
        ));
    }

    private void updateLog(SearchState s) {
        int underestimate = s.getUnderestimate();
        if (underestimate < currentUpperBound) {
            currentUpperBound = underestimate;
            result = s;
        }
    }

}
