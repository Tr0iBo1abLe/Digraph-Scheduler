package Solver;

import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import lombok.Data;

import java.util.stream.IntStream;


/**
 * Created by mason on 31/07/17.
 */
@Data
public final class DFSSolver extends AbstractSolver {

    private int currMax = Integer.MAX_VALUE;
    private SearchState result;

    public DFSSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
    }

    @Override
    public void doSolve() {
        SearchState.initialise(graph);
        SearchState s = new SearchState();
        solving(s);
        scheduleVertices(result);

    }

    private void solving(SearchState s) {
        s.getLegalVertices().forEach(v -> IntStream.range(0, processorCount).forEach(i -> {
                    SearchState next = new SearchState(s, v, i);
                    if (next.getPriority() >= currMax) {
                        return;
                    }
                    if (next.getSize() == graph.getVertices().size()) {
                        updateLog(next, next.getPriority());
                        return;
                    }
                    solving(next);
                }
        ));
    }

    private void updateLog(SearchState s, int cost) {
        if (cost < currMax) {
            currMax = cost;
            result = s;
        }
    }

}
