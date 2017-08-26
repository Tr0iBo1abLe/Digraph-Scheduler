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
     * BnB is faster when:
     * 1 processor
     * few edges (sparse graph), at the moment only picked for 0 edges.
     * <p>
     * Note: A* has a fail safe and will switch to DFS when it estimates a memory error is coming.
     */
    public ISolver createSolver() {
        ISolver solver;
        // Getting data about the input
        int numEdges = (int) graph.getInwardEdgeMap().values().parallelStream().filter(List::isNotEmpty).count();

        if(parallelCount > 1) {
            // These decisions are in priority order
            if (processorCount == 1) { // BnB since upper bound is that of using one core (topological sort)
                solver = new DFSSolverParallel(graph, processorCount, parallelCount);
            } else if (numEdges < 1) { // No edges, experimenting with this
                solver = new DFSSolverParallel(graph, processorCount, parallelCount);
            } else {
                solver = new AStarSolverParallelJavaExecutor(graph, processorCount, parallelCount);
            }
        }
        else {
            if (processorCount == 1) { // BnB since upper bound is that of using one core (topological sort)
                solver = new DFSSolver(graph, processorCount);
            } else if (numEdges < 1) { // No edges, experimenting with this
                solver = new DFSSolver(graph, processorCount);
            } else {
                solver = new AStarSolver(graph, processorCount);
            }
        }
        log.info("Initialising: " + solver.getClass().getName());
        return solver;
    }
}
