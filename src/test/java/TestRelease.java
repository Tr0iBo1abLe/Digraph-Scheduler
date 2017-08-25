import CommonInterface.ISolver;
import Solver.AStarSolver;
import Solver.DFSSolverParallel;
import Solver.SolverFactory;
import TestCommon.CommonTester;
import Util.Helper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static TestCommon.TestConfig.TEST_FILE_PATH;
import static TestCommon.TestConfig.TEST_RELEASE;
import static org.junit.Assert.assertTrue;

/**
 * Test pruning, valid schedules etc. For final release.
 */
public class TestRelease {

    static {
        org.apache.log4j.BasicConfigurator.configure();
    }

    private ISolver solver;
    private CommonTester tester = new CommonTester(DFSSolverParallel.class); // to get expected output for new input via. pure brute force

    /**
     * Big input, testing for release. Will ensure output is valid + optimal.
     */
    @Ignore
    public void test32Nodes() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_RELEASE + "Nodes_32_Edges_33.dot")), 8, 1).createSolver();
        solver.doSolveAndCompleteSchedule();
        Assert.assertEquals(3, solver.getFinalTime());
    }

    /**
     * Run on 8 processor.
     * Expected = ??? Current lowest 400. TODO check what actual optimal is and that solver produces valid schedule.
     */
    @Ignore
    public void test20Nodes() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_RELEASE + "Nodes_20.dot")), 8, 1).createSolver();
        solver.doSolveAndCompleteSchedule();
        Assert.assertEquals(400, solver.getFinalTime());
        assertTrue(solver instanceof AStarSolver); // no edges
    }

    @Test
    public void test20NodesCheckExpected() {
        solver = tester.doTest(8, new File(TEST_FILE_PATH + TEST_RELEASE + "Nodes_20.dot"));
        solver.doSolveAndCompleteSchedule();
        Assert.assertEquals(400, solver.getFinalTime());
    }
}
