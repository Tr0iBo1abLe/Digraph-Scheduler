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
    @Getter @Setter
    protected GUIUpdater updater;
    @Getter
    private int finalTime;

    @Synchronized
    protected void scheduleVertices(SearchState completeSchedule) {
        finalTime = completeSchedule.getUnderestimate();
        graph.scheduleVertices(completeSchedule);
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
