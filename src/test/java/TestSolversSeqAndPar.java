import Exporter.GraphExporter;
import Solver.AStarSolver;
import Solver.AStarSolverPar;
import Solver.AbstractSolver;
import Solver.DFSSolver;
import TestCommon.CommonTester;
import lombok.extern.log4j.Log4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import static TestCommon.TestConfig.*;
import static junit.framework.TestCase.assertEquals;

/**
 * Unit tests for the solvers (A*, BnB).
 * <p>
 * Note: only the final times are confirmed for the milestone1 tests, the actual output isn't.
 * Determining what the expected output is can be time consuming so this will only be tested on smaller graphs.
 * <p>
 * Created by will on 7/31/17.
 *
 * @author Will Molloy
 */
@Log4j
@RunWith(Parameterized.class)
public class TestSolversSeqAndPar {

    private AbstractSolver solver;
    private CommonTester tester;

    public TestSolversSeqAndPar(CommonTester tester) {
        this.tester = tester;
    }

    @Parameters(name = "{0}") // tester.toString()
    public static Collection data() {
        org.apache.log4j.BasicConfigurator.configure();
        return Arrays.asList(new CommonTester(AStarSolver.class), new CommonTester(DFSSolver.class), new CommonTester(AStarSolverPar.class));
    }

    /**
     * This test ensures the schedule is valid as the solver may place nodes on other cores in parallel when it
     * cannot. The optimal schedule here only uses 1 core.
     */
    @Test
    public void testStraightLine() {
        solver = tester.doTest(6, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_straightline_4nodes.dot"));
        assertEquals(12, solver.getFinalTime());
    }

    /**
     * Tests for Milestone 1 input provided on canvas. Processors = 2.
     */
    @Test
    public void testMilestone1Nodes7Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot"));
        assertEquals(28, solver.getFinalTime());
    }

    @Test
    public void testMilestone1Nodes8Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot"));
        assertEquals(581, solver.getFinalTime());
    }

    @Test
    public void testMilestone1Nodes9Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot"));
        assertEquals(55, solver.getFinalTime());
    }

    @Test
    public void testMilestone1Nodes10Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot"));
        assertEquals(50, solver.getFinalTime());
    }

    @Test
    public void testMilestone1Nodes11Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot"));
        assertEquals(350, solver.getFinalTime());
    }

    /**
     * Tests for Milestone 1 input provided on canvas. Processors = 4.
     */
    @Test
    public void testMilestone1Nodes7Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot"));
        assertEquals(22, solver.getFinalTime());
    }

    @Test
    public void testMilestone1Nodes8Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot"));
        assertEquals(581, solver.getFinalTime());
    }

    @Test
    public void testMilestone1Nodes9Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot"));
        assertEquals(55, solver.getFinalTime());
    }

    @Test
    public void testMilestone1Nodes10Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot"));
        assertEquals(50, solver.getFinalTime());
    }

    @Test
    public void testMilestone1Nodes11Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot"));
        assertEquals(227, solver.getFinalTime());
    }

    /*
     * Edge cases to break our optimisations - they currently do not. Online example does however.
     */
    @Test
    public void testExcludeProcessorsEdgesWithZeroCost() {
        solver = tester.doTest(8, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_4Nodes_ZeroEdgeCosts.dot"));
        log.debug(GraphExporter.exportGraphToString(solver.getGraph()));
        assertEquals(3, solver.getFinalTime());
    }

    @Test
    public void testExcludeProcessors6NodeDiamondWithBranch() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_6nodes_diamond_lowcosts.dot"));
        log.debug(GraphExporter.exportGraphToString(solver.getGraph()));
        assertEquals(4, solver.getFinalTime());
    }

    @Test
    public void testExcludeStartTimes3NodesCShouldBeCore2() {
        solver = tester.doTest(8, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_3Nodes_test_startTimes.dot"));
        log.debug(GraphExporter.exportGraphToString(solver.getGraph()));
        assertEquals(2, solver.getFinalTime());
    }

    /*
     * Online examples
     */

    /**
     * https://www.math.unl.edu/~mbrittenham2/classwk/203f07/quiz/203words7.pdf
     * Note they don't use edge costs (the numbers they have represent a task priority)
     * Therefore the answer they give is incorrect, it can be done in 2 less time units for our rules.
     * (These have been confirmed using our nice GUI)
     */
    @Test
    public void test14NodesUoN2Core() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_14Nodes_203words7dvi_0edgecost.dot"));
        log.debug(GraphExporter.exportGraphToString(solver.getGraph()));
        assertEquals(72, solver.getFinalTime());
    }

    public void test14NodeUoN3Core() {
        solver = tester.doTest(3, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_14Nodes_203words7dvi_0edgecost.dot"));
        log.debug(GraphExporter.exportGraphToString(solver.getGraph()));
        assertEquals(57, solver.getFinalTime());
    }

    /**
     * https://www.math.unl.edu/~mbrittenham2/classwk/203f07/quiz/203words7.pdf
     * Note: not all the edges were added; I made a mistake adding them and accidentally found a testcase that breaks our solution!!
     * Added edge costs (I thought that's what they were at first)
     * This test breaks the 'exclude "startTimes"' in SearchState class.
     * <p>
     * The expected finalTime is 96, "exclude startTimes" gives 98
     * This is because with "exclude startTimes" the priority queue is adding a state that is treated as equal (incorrectly)
     * to the state(s) that are required to produce the optimal schedule. Therefore the pre state to the optimal state is
     * not considered.
     */
    @Test
    public void test14NodesUoN2CoreWithEdgeCost() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_14Nodes_3CoreOptimal.dot"));
        log.debug(GraphExporter.exportGraphToString(solver.getGraph()));
        assertEquals(96, solver.getFinalTime());
    }

    /**
     * Attempt to break exclude "startTimes" vs. excluding nothing rather than excluding "processors" vs excluding both.
     * I'm sure excluding startTimes is incorrect (queue size of 40k vs. 700k) but it would be ideal to have an actual test case to prove this!
     * <p>
     * Excluding processors gives a mirrored schedule as excluding nothing, while exclude startTimes gives an altered version.
     * Excluding processors gives a queue size of base_case/processor_count, where base_case is excluding nothing from SearchState.equals()
     * so clearly it only ignoring mirror schedules while exclude startTimes is ignoring possible optimal states!
     */
    @Test
    public void test14NodesUoN2CoreWithEdgeCostV2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_14Nodes_3CoreOptimal-1.dot"));
        log.debug(GraphExporter.exportGraphToString(solver.getGraph()));
        assertEquals(96, solver.getFinalTime());
    }


}
