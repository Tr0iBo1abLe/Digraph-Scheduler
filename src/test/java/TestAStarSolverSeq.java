import Exporter.GraphExporter;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Parser.EdgeCtor;
import Parser.InputParser;
import Parser.VertexCtor;
import Solver.AStarSolver;
import Solver.AbstractSolver;
import TestCommon.CommonTester;
import Util.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static TestCommon.TestConfig.TEST_FILE_PATH;
import static TestCommon.TestConfig.TEST_MILESTONE_1_INPUT_PATH;
import static TestCommon.TestConfig.TEST_SOLVER_PATH;
import static junit.framework.TestCase.assertEquals;

/**
 * Unit tests for the A* Solver implementation that's sequential (no parallel programming is tested here).
 * <p>
 * This tests a possible solution for Milestone1 where we need a valid schedule (doesn't have to be optimal) and
 * doesn't need to be parallel.
 * <p>
 * Note: the final time is confirmed for the optimal schedule but coming up with the actual optimal schedule
 * takes a while so only asserting the final test time for now. TODO Possible solution to this is an isValidSchedule() method.
 * <p>
 * Created by will on 7/31/17.
 */
public class TestAStarSolverSeq {

    private Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private InputParser<Vertex, EdgeWithCost<Vertex>> parser;
    private AbstractSolver solver;
    private CommonTester tester;

    @Before
    public void setup() {
        tester = new CommonTester(AStarSolver.class);
        graph = new Graph<Vertex, EdgeWithCost<Vertex>>();
        parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
    }

    /**
     * This test ensures the schedule is valid as the solver may place nodes on other cores in parallel when it
     * cannot. The optimal schedule here only uses 1 core.
     */
    @Test
    public void testStraightLine() {
        graph = parser.doParseAndFinaliseGraph(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_straightline_4nodes.dot");
        solver = new AStarSolver(graph, 4); // Must construct solver only after graph has been parsed in.
        solver.doSolve();

        String actual = GraphExporter.exportGraphToString(graph);
        String expected = FileUtils.readFileToString(TEST_FILE_PATH + TEST_SOLVER_PATH + "output_straightline_4nodes.dot");
        assertEquals(expected, actual);
    }

    /**
     * This test ensures multiple cores are being used when they are required for the optimal schedule as there are no
     * dependencies between nodes.
     */
    @Test
    public void test8Nodes0Edges() {
        solver = tester.doTest(8, new File(TEST_FILE_PATH + TEST_SOLVER_PATH + "input_8nodes_0edges.dot"));
        assertEquals(3, solver.getFinalTime()); // test final time
    }

    /**
     * Tests for Milestone 1 input provided on canvas. Processors = 2.
     */
    @Test
    public void testMilestone1Nodes7Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot"));
        assertEquals(28, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes8Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot"));
        assertEquals(581, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes9Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot"));
        assertEquals(55, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes10Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot"));
        assertEquals(50, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes11Processors2() {
        solver = tester.doTest(2, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot"));
        assertEquals(350, solver.getFinalTime()); // test final time
    }

    /**
     * Tests for Milestone 1 input provided on canvas. Processors = 4.
     */
    @Test
    public void testMilestone1Nodes7Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot"));
        assertEquals(22, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes8Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot"));
        assertEquals(581, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes9Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot"));
        assertEquals(55, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes10Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot"));
        assertEquals(50, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes11Processors4() {
        solver = tester.doTest(4, new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot"));
        assertEquals(227, solver.getFinalTime()); // test final time
    }

    /**
     * Attempting to use ALOT of memory with many search states.
     */
    @Test
    public void hugeSearchStateUseUpMemory(){

    }

}
