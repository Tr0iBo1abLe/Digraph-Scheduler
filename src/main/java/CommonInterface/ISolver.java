package CommonInterface;

import GUI.Interfaces.IUpdatableState;
import Graph.Vertex;
import Graph.EdgeWithCost;
import Graph.Graph;

import java.util.Timer;

/**
 * Created by e on 30/07/17.
 */
public interface ISolver {
    void doSolveAndCompleteSchedule();

    void associateUI(IUpdatableState ui);

    int getProcessorCount();

    int getFinalTime();

    Timer getTimer(); // get the gui timer required by GUI components see #Field Timer timer in #AbstractSolver

    Graph<Vertex, EdgeWithCost<Vertex>> getGraph();
}
