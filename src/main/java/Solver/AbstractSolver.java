package Solver;

import CommonInterface.ISearchState;
import CommonInterface.ISolver;
import GUI.IUpdatableState;
import Graph.EdgeWithCost;
import Graph.Exceptions.GraphException;
import Graph.Graph;
import Graph.Vertex;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

import java.util.Arrays;

/**
 * Created by e on 1/08/17.
 */
@Data
abstract public class AbstractSolver implements ISolver {
    protected final Graph<Vertex, EdgeWithCost<Vertex>> graph;
    protected final int processorCount;
    @Getter
    @Setter
    protected GUIUpdater updater;
    @Getter
    private int finalTime;

    @Synchronized
    protected void scheduleVertices(SearchState completeSchedule) {
        final int[] processors = Arrays.stream(completeSchedule.getProcessors()).map(x -> x + 1).toArray();
        final int[] startTimes = completeSchedule.getStartTimes();
        finalTime = completeSchedule.getUnderestimate();

        graph.getVertices().forEach(vertex -> {
            int id = vertex.getAssignedId();
            try {
                graph.scheduleVertex(vertex, processors[id], startTimes[id]);
            } catch (GraphException e) {
                e.printStackTrace();
            }
        });
    }

    public void associateUI(IUpdatableState ui) {
        this.updater = new GUIUpdater(ui);
    }

    protected static class GUIUpdater {
        IUpdatableState ui;

        protected GUIUpdater(IUpdatableState ui) {
            this.ui = ui;
        }

        protected void update(ISearchState searchState) {
            ui.updateWithState(searchState);
        }
    }
}
