package TestCommon;

import Graph.Graph;
import Solver.AbstractSolver;
import Util.Helper;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 * Makes use of reflection, allowing test classes to be simpler by feeding input to any type of solver.
 * Now the type of solver only needs to be specified once in a test class rather than in every test.
 * Tests simply need to pass the PROCESSOR_COUNT and DOT file to a doTest() call.
 *
 * @author Will Molloy, wmol664
 */
public class CommonTester {

    private Class<? extends AbstractSolver> solverClass;
    private AbstractSolver solver;

    /**
     * Configures the CommonTester object to the solver class it will be feeding input to.
     *
     * @param solverClass, a concrete implementation of AbstractSolver.
     */
    public CommonTester(Class<? extends AbstractSolver> solverClass) {
        this.solverClass = solverClass;
    }

    /**
     * Ensure this Class is initialised with a valid solver.
     */
    private CommonTester() {
    }

    /**
     * Initialises the solver with the given processor count and DOT file and then calls doSolve().
     *
     * @param processorCount, number of cores available to the output schedule
     * @param inputDOTFile,   the input DOT file
     */
    public AbstractSolver doTest(int processorCount, File inputDOTFile) {
        Graph graph = Helper.fileToGraph(inputDOTFile);

        try {
            solver = solverClass.getDeclaredConstructor(Graph.class, int.class).newInstance(graph, processorCount);
            solver.doSolveAndCompleteSchedule();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        assert solver != null;
        return solver; // so we can analyse output with JUnit
    }

    @Override
    public String toString() {
        return solverClass.getName();
    }
}
