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
public class DFSSolver extends AbstractSolver {

    private static int log = Integer.MAX_VALUE;
    private static SearchState result;

    public DFSSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
    }

    @Override
    public void doSolve() {
        SearchState.init(graph);
        SearchState s = new SearchState();
        solving(s);
        scheduleVertices(result);

    }

    private void solving(SearchState s) {
        s.getLegalVertices().stream().forEach(v -> IntStream.of(0, processorCount - 1).forEach(i -> {
                    SearchState next = new SearchState(s, v, i);
                    if (next.getDFcost() >= log) {
                        return;
                    }
                    if (next.getSize() == graph.getVertices().size()) {
                        updateLog(next, next.getDFcost());
                        return;
                    }
                    solving(next);
                }
        ));
    }

    private void updateLog(SearchState s, int cost) {
        if (cost < log) {
            log = cost;
            result = s;
        }
    }

}
