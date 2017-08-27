import CommonInterface.ISolver;
import Exporter.GraphExporter;
import Solver.AStarSolver;
import Solver.SolverFactory;
import TestCommon.CommonTester;
import Util.Helper;
import lombok.extern.log4j.Log4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static TestCommon.TestConfig.TEST_FILE_PATH;
import static TestCommon.TestConfig.TEST_RELEASE;
import static org.junit.Assert.assertTrue;

/**
 * Test pruning, valid schedules etc. For final release.
 * Use the factory since that is what'll be used on release.
 *
 * @author Will Molloy
 */
@Log4j
@Ignore // Ignore: Time consuming for CI and other tests are sufficient for ensuring nothing is broken. Run these individually.
public class TestRelease {

    static {
        org.apache.log4j.BasicConfigurator.configure();
    }

    private ISolver solver;
    private CommonTester tester;

    /**
     * Big input, testing for release. Need to ensure output is valid + optimal.
     */
    @Ignore
    public void test32Nodes() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_RELEASE + "Nodes_32_Edges_33.dot")), 8, 1).createSolver();
        solver.doSolveAndCompleteSchedule();
        Assert.assertEquals(3, solver.getFinalTime());
    }

    /**
     * Run on 8 processor.
     * Expected = 397
     */
    @Test
    public void test20Nodes() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_RELEASE + "Nodes_20.dot")), 8, 1).createSolver();
        solver.doSolveAndCompleteSchedule();
        log.debug(GraphExporter.exportGraphToString(solver.getGraph()));
        Assert.assertEquals(397, solver.getFinalTime());
    }

    @Test
    public void test3Nodes() {
        solver = new CommonTester(AStarSolver.class).doSequentialTest(3, new File(TEST_FILE_PATH + TEST_RELEASE + "Nodes_3.dot"));
        log.debug(GraphExporter.exportGraphToString(solver.getGraph()));
        Assert.assertEquals(10, solver.getFinalTime());
    }

    @Test
    public void test3Nodes2Edge() {
        solver = new CommonTester(AStarSolver.class).doSequentialTest(2, new File(TEST_FILE_PATH + TEST_RELEASE + "Nodes_3_Edges_2.dot"));
        log.debug(GraphExporter.exportGraphToString(solver.getGraph()));
        Assert.assertEquals(40, solver.getFinalTime());
    }

}
