package Solver;

import CommonInterface.ISolver;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import fj.data.List;
import lombok.Getter;
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
public class SolverFactory {

    private Solver solver;
    @Getter
    private Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private int processorCount;

    public SolverFactory(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount){
        this.graph = graph;
        this.processorCount = processorCount;
    }

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
        // Getting data about the input
        int numEdges = (int) graph.getInwardEdgeMap().values().parallelStream().filter(List::isNotEmpty).count();

        // "AI is just a bunch of if/then statements"
        // These decisions are in priority order
        if (processorCount == 1) { // BnB since upper bound is that of using one core
            solver = Solver.BnB;
        } else if (numEdges < 1) { // No edges, experimenting with this
            solver = Solver.BnB;
        } else {
            solver = Solver.AStar;
        }
        log.debug("Initialising: " + solver.getSolver().getClass().getName());
        return initialiseSolver();
    }

    private AbstractSolver initialiseSolver() {
        try {
            return solver.getSolver().getDeclaredConstructor(Graph.class, int.class).newInstance(graph, processorCount);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * List of available Solvers. (Parallel is to be added..)
     */
    private enum Solver {
        AStar(AStarSolver.class), BnB(DFSSolver.class); // link type to actual class; avoids switch-case
        private Class<? extends AbstractSolver> solver;

        Solver(Class<? extends AbstractSolver> solver) {
            this.solver = solver;
        }

        public Class<? extends AbstractSolver> getSolver() {
            return solver;
        }
    }

}
