import Solver.AbstractSolver;
import Solver.SmartSolver;
import TestCommon.CommonTester;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


import java.io.File;
import java.util.Collection;
import java.util.Collections;

import static TestCommon.TestConfig.TEST_FILE_PATH;
import static TestCommon.TestConfig.TEST_MILESTONE_1_INPUT_PATH;
import static TestCommon.TestConfig.TEST_SOLVER_PATH;
import static junit.framework.TestCase.assertEquals;

/**
 * Test the SmartSolver picks the best solver to use.
 * TODO time the tests and make sure SmartSolver is indeed picking the faster one.
 *
 * Created by will on 8/21/17.
 * @author Will Molloy
 */
@RunWith(Parameterized.class)
public class TestSmartSolver {

    private AbstractSolver solver;
    private CommonTester tester;

    public TestSmartSolver(CommonTester tester){
        this.tester = tester;
    }

    @Parameters(name = "{0}") // tester.toString()
    public static Collection data() {
        org.apache.log4j.BasicConfigurator.configure();
        return Collections.singletonList(new CommonTester(SmartSolver.class));
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
     * This test ensures multiple cores are being used when they are required for the optimal schedule as there are no
     * dependencies between nodes.
     */
    @Test
    public void test8Nodes0Edges() {
        solver = tester.doTest(8, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_8nodes_0edges.dot"));
        assertEquals(3, solver.getFinalTime());
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

    @Ignore
    public void memoryTest96Nodes1Core(){
        solver = tester.doTest(1, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_96nodes_0edges.dot"));
        Assert.assertEquals(288, solver.getFinalTime());
    }

    @Ignore
    public void memoryTest96Nodes96Core(){
        solver = tester.doTest(96, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_96nodes_0edges.dot"));
        Assert.assertEquals(3, solver.getFinalTime());
    }

    @Test
    public void memoryTest48Nodes(){
        solver = tester.doTest(48, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_48nodes_0edges.dot"));
        Assert.assertEquals(3, solver.getFinalTime());
    }

    @Test
    public void memoryTest24Nodes(){
        solver = tester.doTest(24, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_24nodes_0edges.dot"));
        Assert.assertEquals(3, solver.getFinalTime());
    }

    @Test
    public void memoryTest16Nodes16Cores(){
        solver = tester.doTest(16, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_16nodes_0edges.dot"));
        Assert.assertEquals(3, solver.getFinalTime());
    }

    @Ignore
    public void memoryTest16Nodes8Cores(){
        solver = tester.doTest(8, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_16nodes_0edges.dot"));
        Assert.assertEquals(6, solver.getFinalTime());
    }

    @Test
    public void memoryTest16PlusNodes(){
        solver = tester.doTest(17, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_16PlusNodes_0edges.dot"));
        Assert.assertEquals(3, solver.getFinalTime());
    }
}
