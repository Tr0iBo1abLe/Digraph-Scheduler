import CommonInterface.ISolver;
import Exporter.GraphExporter;
import Solver.AStarSolver;
import Solver.SolverFactory;
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
@Ignore
public class TestRelease {

    static {
        org.apache.log4j.BasicConfigurator.configure();
    }

    private ISolver solver;

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
    @Test
    public void test20Nodes() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_RELEASE + "Nodes_20.dot")), 8, 1).createSolver();
        solver.doSolveAndCompleteSchedule();
        System.out.println(GraphExporter.exportGraphToString(solver.getGraph())); // log doesnt work
        Assert.assertEquals(400, solver.getFinalTime());
        assertTrue(solver instanceof AStarSolver); // no edges
    }


}
