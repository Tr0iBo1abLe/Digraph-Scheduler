package Solver;

import CommonInterface.ISolver;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import fj.data.List;
import lombok.Getter;
import lombok.Value;
import lombok.extern.log4j.Log4j;

import java.lang.reflect.InvocationTargetException;

/**
 * Picks the best solver (A*, BnB) to run based on the input.
 * This is required since we cannot manually pick the solver ourselves.
 * BnB is needed because A* has memory problems for large input @see TestMemoryUsage.
 *
 * @author Will Molloy
 */
@Log4j
@Value
public class SolverFactory {
    @Getter
    private Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private int processorCount;
    private int parallelCount;

    /**
     * Picks the solver to use based on the program arguments.
     * <p>
     * TODO Current ideas:
     * Get memory available to the JVM (machine dependent)
     * get average memory size of search state (depends on num vertices)
     * estimate number of search states (i.e. size of A* queue)
     * if this exceeds available memory BnB will be selected.
     * <p>
     * Fail safe: switch to BnB if A* exceeds heap memory.
     * <p>
     * But there are some cases where BnB is faster even when A* terminates:
     * 1 processor
     * few edges (sparse graph)
     * .. any more?
     */
    public ISolver createSolver() {
        ISolver solver;
        // Getting data about the input
        int numEdges = (int) graph.getInwardEdgeMap().values().parallelStream().filter(List::isNotEmpty).count();

        if(parallelCount > 1) {
            solver = new AStarSolverParallelJavaExecutor(graph, processorCount, parallelCount);
        }
        else {
            // "AI is just a bunch of if/then statements"
            // These decisions are in priority order
            if (processorCount == 1) { // BnB since upper bound is that of using one core
                solver = new DFSSolver(graph, processorCount);
            } else if (numEdges < 1) { // No edges, experimenting with this
                solver = new DFSSolver(graph, processorCount);
            } else {
                solver = new AStarSolver(graph, processorCount);
            }
            log.debug("Initialising: " + solver.getClass().getName());
        }
        return solver;
    }
    // Reflection is kind of unnecessary here
}
