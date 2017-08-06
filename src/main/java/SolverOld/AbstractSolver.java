package SolverOld;

import CommonInterface.ISolver;
import Graph.EdgeWithCost;
import Graph.Exceptions.GraphException;
import Graph.Graph;
import Graph.Vertex;
import lombok.Data;

import java.util.Arrays;

/**
 * Created by e on 1/08/17.
 */
@Data
abstract public class AbstractSolver implements ISolver {
    protected final Graph<Vertex, EdgeWithCost<Vertex>> graph;
    protected final int processorCount;

    protected void scheduleVertices(SearchState s) {
        final int[] processors = Arrays.stream(s.getProcessors()).map(x -> x+1).toArray();
        final int[] startTimes = s.getStartTimes();
        graph.getVertices().forEach(v -> {
            int id = v.getAssignedId();
            try {
                graph.scheduleVertex(v ,processors[id], startTimes[id]);
            } catch (GraphException e) {
                e.printStackTrace();
            }
        });
    }
}
