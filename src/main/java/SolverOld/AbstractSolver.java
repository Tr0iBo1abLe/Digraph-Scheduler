package SolverOld;

import CommonInterface.ISearchState;
import CommonInterface.ISolver;
import GUI.IUpdatableState;
import Graph.EdgeWithCost;
import Graph.Exceptions.GraphException;
import Graph.Graph;
import Graph.Vertex;
import lombok.Data;
import lombok.Getter;

import java.util.Arrays;

/**
 * Created by e on 1/08/17.
 */
@Data
abstract public class AbstractSolver implements ISolver {
    @Getter
    protected final Graph<Vertex, EdgeWithCost<Vertex>> graph;
    @Getter
    protected final int processorCount;
    protected GUIUpdater updater;

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

    protected static class GUIUpdater {
        IUpdatableState ui;
        public GUIUpdater(IUpdatableState ui) { this.ui = ui; }
        public void update(ISearchState searchState) {
            ui.updateWithState(searchState);
        }
    }

    public void associateUI(IUpdatableState ui) {
        this.updater = new GUIUpdater(ui);
    }
}
