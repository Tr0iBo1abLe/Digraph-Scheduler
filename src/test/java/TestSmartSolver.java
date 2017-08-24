import Solver.AStarSolver;
import Solver.AbstractSolver;
import Solver.DFSSolver;
import Solver.SmartSolver;
import TestCommon.CommonTester;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static TestCommon.TestConfig.*;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the SmartSolver picks the best solver to use.
 * <p>
 * Created by will on 8/21/17.
 *
 * @author Will Molloy
 */
public class TestSmartSolver {

    static {
        org.apache.log4j.BasicConfigurator.configure();
    }

    private AbstractSolver solver;
    private CommonTester tester = new CommonTester(SmartSolver.class);

    private AbstractSolver getSolverType() {
        if (solver instanceof SmartSolver) {
            return ((SmartSolver) solver).getCurrentSolver();
        }
        return null;
    }

    /**
     * This test ensures the schedule is valid as the solver may place nodes on other cores in parallel when it
     * cannot. The optimal schedule here only uses 1 core.
     */
    @Test
    public void testStraightLine() {
        solver = tester.doTest(6, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_straightline_4nodes.dot"));
        assertEquals(12, solver.getFinalTime());
        assertTrue(getSolverType() instanceof AStarSolver); // DFS may be faster here..?
    }

    /**
     * Tests for Milestone 1 input provided on canvas. Processors = 2.
     */
    @Test
    public void testMilestone1Nodes7Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot"));
        assertEquals(28, solver.getFinalTime());
        assertTrue(getSolverType() instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes8Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot"));
        assertEquals(581, solver.getFinalTime());
        assertTrue(getSolverType() instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes9Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot"));
        assertEquals(55, solver.getFinalTime());
        assertTrue(getSolverType() instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes10Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot"));
        assertEquals(50, solver.getFinalTime());
        assertTrue(getSolverType() instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes11Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot"));
        assertEquals(350, solver.getFinalTime());
        assertTrue(getSolverType() instanceof AStarSolver);
    }

    /**
     * Tests for Milestone 1 input provided on canvas. Processors = 4.
     */
    @Test
    public void testMilestone1Nodes7Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot"));
        assertEquals(22, solver.getFinalTime());
        assertTrue(getSolverType() instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes8Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot"));
        assertEquals(581, solver.getFinalTime());
        assertTrue(getSolverType() instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes9Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot"));
        assertEquals(55, solver.getFinalTime());
        assertTrue(getSolverType() instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes10Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot"));
        assertEquals(50, solver.getFinalTime());
        assertTrue(getSolverType() instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes11Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot"));
        assertEquals(227, solver.getFinalTime());
        assertTrue(getSolverType() instanceof AStarSolver);
    }

    /**
     * Memory tests (BnB favoured)
     */

    @Test
    public void memoryTest96Nodes1Core() {
        solver = tester.doTest(1, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_96nodes_0edges.dot"));
        Assert.assertEquals(288, solver.getFinalTime());
        assertTrue(getSolverType() instanceof DFSSolver);
    }

    @Test
    public void memoryTest96Nodes96Core() {
        solver = tester.doTest(96, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_96nodes_0edges.dot"));
        Assert.assertEquals(3, solver.getFinalTime());
        assertTrue(getSolverType() instanceof DFSSolver); // no edges
    }

    @Test
    public void memoryTest48Nodes() {
        solver = tester.doTest(48, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_48nodes_0edges.dot"));
        Assert.assertEquals(3, solver.getFinalTime());
        assertTrue(getSolverType() instanceof DFSSolver);
    }

    @Test
    public void memoryTest24Nodes() {
        solver = tester.doTest(24, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_24nodes_0edges.dot"));
        Assert.assertEquals(3, solver.getFinalTime());
        assertTrue(getSolverType() instanceof DFSSolver);
    }

    @Test
    public void memoryTest16Nodes16Cores() {
        solver = tester.doTest(16, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_16nodes_0edges.dot"));
        Assert.assertEquals(3, solver.getFinalTime());
        assertTrue(getSolverType() instanceof DFSSolver);
    }

    @Ignore
    public void memoryTest16Nodes8Cores() {
        solver = tester.doTest(8, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_16nodes_0edges.dot"));
        Assert.assertEquals(6, solver.getFinalTime());
    }

    @Test
    public void memoryTest16PlusNodes() {
        solver = tester.doTest(17, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_16PlusNodes_0edges.dot"));
        Assert.assertEquals(3, solver.getFinalTime());
        assertTrue(getSolverType() instanceof DFSSolver);
    }
}
