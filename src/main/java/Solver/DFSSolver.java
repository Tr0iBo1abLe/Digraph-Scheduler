package Solver;

import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import lombok.Data;

import java.util.stream.IntStream;


/**
 * DFS Solver, uses branch and bound technique to prune search states in the stack.
 * Doesn't use much memory since the current best state is cached while the stack is cleared on each iteration.
 *
 * Created by mason on 31/07/17.
 * @author Mason Shi, Edward Huang, Will Molloy
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
        // Upper bound is initially all the nodes scheduled to one processor (when edge cost can be ignored)
        currentUpperBound = graph.getVertices().parallelStream().mapToInt(Vertex::getCost).sum();
        SearchState s = new SearchState();
        solving(s);
        scheduleVertices(result);
    }

    private void solving(SearchState s) {
        s.getLegalVertices().forEach(v -> IntStream.range(0, processorCount).forEach(i -> {
                    SearchState next = new SearchState(s, v, i);
                    if (result != null && next.getUnderestimate() >= currentUpperBound) {
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
        if (underestimate <= currentUpperBound) { // MUST BE <=, for the case where result hasn't been initialised
            currentUpperBound = underestimate;
            result = s;
        }
    }

}
