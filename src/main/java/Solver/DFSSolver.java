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

    private int currentUpperBound;
    private SearchState result;

    public DFSSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
    }

    @Override
    public void doSolve() {
        SearchState.initialise(graph);
        // ideally initial upperbound would be topological sort length
        currentUpperBound = graph.getVertices().stream().filter(o -> o.getCost() > 0).mapToInt(Vertex::getCost).sum();
        currentUpperBound += graph.getForwardEdges().stream().filter(o -> o.getCost() > 0).mapToInt(EdgeWithCost::getCost).sum();
        SearchState s = new SearchState();
        solving(s);
        scheduleVertices(result);
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
