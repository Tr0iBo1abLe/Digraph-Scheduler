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
 * Represents all Solvers holding the program arguments: graph and processorCount.
 * Contains public method for solving the scheduling problem.
 *
 * Created by e on 1/08/17.
 * @author Edward Huang, Will Molloy
 */
@Data
abstract public class AbstractSolver implements ISolver {
    protected Graph<Vertex, EdgeWithCost<Vertex>> graph;
    protected int processorCount;
    @Getter
    @Setter
    protected GUIUpdater updater;
    protected SearchState currBestState;
    @Getter
    private int finalTime;

    /**
     * This class should only be instantiated by the concrete algorithms.
     */
    AbstractSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        this.graph = graph;
        this.processorCount = processorCount;
        SearchState.initialise(graph);
        currBestState = new SearchState();
    }

    /**
     * When initialising another algorithm.
     */
    AbstractSolver() {
    }

    /**
     * Template method.
     * Calls the concrete implementation of doSolve() depending on the required algorithm, then schedules the tasks.
     */
    public final void doSolveAndCompleteSchedule() {
        doSolve();
        scheduleVertices();
    }

    abstract void doSolve();

    @Synchronized
    private void scheduleVertices() {
        final int[] processors = Arrays.stream(currBestState.getProcessors()).map(x -> x + 1).toArray();
        final int[] startTimes = currBestState.getStartTimes();
        finalTime = currBestState.getUnderestimate();

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

        GUIUpdater(IUpdatableState ui) {
            this.ui = ui;
        }

        void update(ISearchState searchState) {
            ui.updateWithState(searchState);
        }
    }
}
