import Solver.AStarSolver;
import Solver.AbstractSolver;
import Util.FileUtils;
import Util.Helper;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static TestCommon.TestConfig.TEST_FILE_PATH;
import static TestCommon.TestConfig.TEST_MILESTONE_1_INPUT_PATH;
import static TestCommon.TestConfig.TEST_SOLVER_PATH;
import static junit.framework.TestCase.assertEquals;

/**
 * Unit tests for the A* Solver implementation that's sequential (no parallel programming is tested here).
 * This is the implementation we will use in MILESTONE 1.
 *
 * Created by will on 7/31/17.
 */
public class TestAStarSolverSeq_M1Tests {

    private static int PROCESSOR_COUNT;
    private Graph graph;
    private AbstractSolver solver;

    @Before
    public void setup() {
        PROCESSOR_COUNT = 4;
        graph = new DefaultGraph("g");
    }

    @Test
    public void testStraightLine(){
        graph = Helper.fileToGsGraph(new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_straightline_4nodes.dot"));
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        String actual = Helper.gsGraphToDOTString(graph);
        String expected = FileUtils.readFileToString(TEST_FILE_PATH + TEST_SOLVER_PATH + "output_straightline_4nodes.dot");

        //   assertEquals(actual, expected); // test output
        assertEquals(12, solver.getFinalTime()); // test final time
    }

    /**
     * This test ensures multiple cores are being used when they are required for the optimal schedule as there are no
     * dependencies between nodes.
     */
    @Test
    public void test8Nodes0Edges(){
        PROCESSOR_COUNT = 8;
        graph = Helper.fileToGsGraph(new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_8nodes_0edges.dot"));
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        String actual = Helper.gsGraphToDOTString(graph);
        String expected = FileUtils.readFileToString(TEST_FILE_PATH + TEST_SOLVER_PATH + "output_8nodes_0edges.dot");

     //   assertEquals(actual, expected); // test output
        assertEquals(3, solver.getFinalTime()); // test final time
    }

    /**
     * Tests for Milestone 1 input provided on canvas.
     * Processors = 2.
     */

    @Test
    public void testMilestone1Nodes7Processors2(){
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGsGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot"));
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(28, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes8Processors2(){
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGsGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot"));
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(581, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes9Processors2(){
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGsGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot"));
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(55, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes10Processors2(){
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGsGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot"));
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(50, solver.getFinalTime()); // test final time
    }

    // TODO Takes a while, pretty sure it's stuck
    @Ignore
    public void testMilestone1Nodes11Processors2(){
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGsGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot"));
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(350, solver.getFinalTime()); // test final time
    }

    /**
     * Tests for Milestone 1 input provided on canvas.
     * Processors = 4.
     */

    @Test
    public void testMilestone1Nodes7Processors4(){
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGsGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot"));
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(22, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes8Processors4(){
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGsGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot"));
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(581, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes9Processors4(){
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGsGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot"));
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(55, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes10Processors4(){
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGsGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot"));
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(50, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes11Processors4(){
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGsGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot"));
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(227, solver.getFinalTime()); // test final time
    }
}