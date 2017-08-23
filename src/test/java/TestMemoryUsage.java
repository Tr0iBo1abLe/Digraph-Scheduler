import Solver.AStarSolver;
import Solver.AStarSolverPar;
import Solver.AbstractSolver;
import Solver.DFSSolver;
import TestCommon.CommonTester;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import static TestCommon.TestConfig.TEST_FILE_PATH;
import static TestCommon.TestConfig.TEST_SOLVER_PATH;
import static org.junit.Assert.assertEquals;


/**
 * Attempting to see how many nodes AStarSolver can handle.
 * We will require branch and bound to cut states out of the priority queue in order to preserve memory.
 * TODO a smart solver that estimates memory usage and picks the solver to use; i.e. when BnB is better than A*
 *
 * The use of memory is the number of search states within the A* queue; therefore the number of nodes is not
 * the only factor contributing to this. ProcessorCount and Edges matter too.
 *
 * The @Ignore tests currently exhaust all memory (8GB system) or take too long (10min+) on the CI tool.
 *
 * @author Will Molloy, wmol664
 */
@RunWith(Parameterized.class)
public class TestMemoryUsage {

    private AbstractSolver solver;
    private CommonTester tester;

    public TestMemoryUsage(CommonTester tester){
        this.tester = tester;
    }

    @Parameters(name = "{0}") // tester.toString()
    public static Collection data() {
        return Arrays.asList(new CommonTester(AStarSolver.class), new CommonTester(DFSSolver.class));
    }

    @Ignore
    public void memoryTest96Nodes1Core(){
        solver = tester.doTest(1, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_96nodes_0edges.dot"));
        assertEquals(288, solver.getFinalTime());
    }

    @Ignore
    public void memoryTest96Nodes96Core(){
        solver = tester.doTest(96, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_96nodes_0edges.dot"));
        assertEquals(3, solver.getFinalTime());
    }

    @Ignore
    public void memoryTest48Nodes(){
        solver = tester.doTest(48, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_48nodes_0edges.dot"));
        assertEquals(3, solver.getFinalTime());
    }

    @Test
    public void memoryTest24Nodes(){
        solver = tester.doTest(24, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_24nodes_0edges.dot"));
        assertEquals(3, solver.getFinalTime());
    }

    @Test
    public void memoryTest16Nodes16Cores(){
        solver = tester.doTest(16, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_16nodes_0edges.dot"));
        assertEquals(3, solver.getFinalTime());
    }

    @Ignore
    public void memoryTest16Nodes8Cores(){
        solver = tester.doTest(8, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_16nodes_0edges.dot"));
        assertEquals(6, solver.getFinalTime());
    }

    @Test
    public void memoryTest16PlusNodes(){
        solver = tester.doTest(17, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_16PlusNodes_0edges.dot"));
        assertEquals(3, solver.getFinalTime());
    }
}
