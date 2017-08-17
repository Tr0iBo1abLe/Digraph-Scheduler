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
    @Getter
    private int finalTime;
    @Getter
    protected boolean isUpdatableProgressBar = false; //make progress bar compatible with both algorithms if necessary
    @Getter
    protected int stateCounter = 0; //essentially a loop counter, used by GUI to update progress bar

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

        protected void update(ISearchState searchState, AbstractSolver solver) {
            ui.updateWithState(searchState, solver);
        }
    }
}
