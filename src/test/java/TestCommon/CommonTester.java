package TestCommon;

import Graph.Graph;
import Solver.AbstractSolver;
import Util.Helper;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 * Allows test classes to be simpler by feeding input to any type of solver.
 *
 * @author Will Molloy, wmol664
 */
public class CommonTester {

    public CommonTester(){
        
    }

    public AbstractSolver doTest(int processorCount, Graph graph, Class<? extends AbstractSolver> solverClass, File inputFile){
        graph = Helper.fileToGraph(inputFile);
        graph.finalise();
        AbstractSolver solver = null;
        try {
            solver = solverClass.getDeclaredConstructor(Graph.class, int.class).newInstance(graph, processorCount);
            solver.doSolve();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        assert solver != null;
        return solver;
    }
}
