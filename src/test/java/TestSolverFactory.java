import CommonInterface.ISolver;
import Solver.AStarSolver;
import Solver.AbstractSolver;
import Solver.DFSSolver;
import Solver.SolverFactory;
import Util.Helper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static TestCommon.TestConfig.*;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the SolverFactory picks the best solver to use.
 * This will be used in the final release!
 * <p>
 * Created by will on 8/21/17.
 *
 * @author Will Molloy
 */
public class TestSolverFactory {

    static {
        org.apache.log4j.BasicConfigurator.configure();
    }

    private ISolver solver;

    /**
     * This test ensures the schedule is valid as the solver may place nodes on other cores in parallel when it
     * cannot. The optimal schedule here only uses 1 core.
     */
    @Test
    public void testStraightLine() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_straightline_4nodes.dot")), 8).createSolver();
        solver.doSolveAndCompleteSchedule();
        assertEquals(12, solver.getFinalTime());
        assertTrue(solver instanceof AStarSolver);
    }

    /**
     * Tests for Milestone 1 input provided on canvas. Processors = 2.
     */
    @Test
    public void testMilestone1Nodes7Processors2() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot")), 2).createSolver();
        solver.doSolveAndCompleteSchedule();
        assertEquals(28, solver.getFinalTime());
        assertTrue(solver instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes8Processors2() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot")), 2).createSolver();
        solver.doSolveAndCompleteSchedule();
        assertEquals(581, solver.getFinalTime());
        assertTrue(solver instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes9Processors2() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot")), 2).createSolver();
        solver.doSolveAndCompleteSchedule();
        assertEquals(55, solver.getFinalTime());
        assertTrue(solver instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes10Processors2() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot")), 2).createSolver();
        solver.doSolveAndCompleteSchedule();
        assertEquals(50, solver.getFinalTime());
        assertTrue(solver instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes11Processors2() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot")), 2).createSolver();
        solver.doSolveAndCompleteSchedule();
        assertEquals(350, solver.getFinalTime());
        assertTrue(solver instanceof AStarSolver);
    }

    /**
     * Tests for Milestone 1 input provided on canvas. Processors = 4.
     */
    @Test
    public void testMilestone1Nodes7Processors4() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot")), 4).createSolver();
        solver.doSolveAndCompleteSchedule();
        assertEquals(22, solver.getFinalTime());
        assertTrue(solver instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes8Processors4() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot")), 4).createSolver();
        solver.doSolveAndCompleteSchedule();
        assertEquals(581, solver.getFinalTime());
        assertTrue(solver instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes9Processors4() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot")), 4).createSolver();
        solver.doSolveAndCompleteSchedule();
        assertEquals(55, solver.getFinalTime());
        assertTrue(solver instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes10Processors4() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot")), 4).createSolver();
        solver.doSolveAndCompleteSchedule();
        assertEquals(50, solver.getFinalTime());
        assertTrue(solver instanceof AStarSolver);
    }

    @Test
    public void testMilestone1Nodes11Processors4() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot")), 4).createSolver();
        solver.doSolveAndCompleteSchedule();
        assertEquals(227, solver.getFinalTime());
        assertTrue(solver instanceof AStarSolver);
    }

    /**
     * Memory tests (BnB favoured)
     */
    @Test
    public void memoryTest96Nodes1Core() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_96nodes_0edges.dot")), 1).createSolver();
        solver.doSolveAndCompleteSchedule();
        Assert.assertEquals(288, solver.getFinalTime());
        assertTrue(solver instanceof DFSSolver); // one core
    }

    @Test
    public void memoryTest96Nodes96Core() {
        solver = new SolverFactory(Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_96nodes_0edges.dot")), 96).createSolver();
        solver.doSolveAndCompleteSchedule();
        Assert.assertEquals(3, solver.getFinalTime());
        assertTrue(solver instanceof DFSSolver); // no edges
    }
}
