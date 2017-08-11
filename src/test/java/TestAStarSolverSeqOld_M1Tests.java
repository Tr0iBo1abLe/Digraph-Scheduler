import Exporter.GraphExporter;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Parser.EdgeCtor;
import Parser.InputParser;
import Parser.VertexCtor;
import SolverOld.AStarSolver;
import SolverOld.AbstractSolver;
import Util.FileUtils;
import Util.Helper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static TestCommon.TestConfig.TEST_FILE_PATH;
import static TestCommon.TestConfig.TEST_MILESTONE_1_INPUT_PATH;
import static junit.framework.TestCase.assertEquals;

/**
 * Unit tests for the A* Solver implementation that's sequential (no parallel programming is tested here).
 * <p>
 * This tests a possible solution for Milestone1 where we need a valid schedule (doesn't have to be optimal) and
 * doesn't need to be parallel.
 * <p>
 * Created by will on 7/31/17.
 */
public class TestAStarSolverSeqOld_M1Tests {

    private static final String TEST_FILES_PATH = "src/test/resources/",
            TEST_SOLVER = "TestSolver/",
            MILESTONE_1_INPUT = "input-graphs-milestone1/",
            MILESTONE_1_OUTPUT = "output-graphs-milestone1/";
    private static int PROCESSOR_COUNT;
    private Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private InputParser<Vertex, EdgeWithCost<Vertex>> parser;
    private AbstractSolver solver;

    @Before
    public void setup() {
        PROCESSOR_COUNT = 2; // Override in individual tests if needed.
        graph = new Graph<Vertex, EdgeWithCost<Vertex>>();
        parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
    }

    /**
     * This test ensures the schedule is valid as the solver may place nodes on other cores in parallel when it
     * cannot. The optimal schedule here only uses 1 core.
     */
    @Test
    public void testStraightLine() {
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + TEST_SOLVER + "input_straightline_4nodes.dot");
        solver = new AStarSolver(graph, PROCESSOR_COUNT); // Must construct solver only after graph has been parsed in.
        solver.doSolve();

        String actual = GraphExporter.exportGraphToString(graph);
        String expected = FileUtils.readFileToString(TEST_FILES_PATH + TEST_SOLVER + "output_straightline_4nodes.dot");
        assertEquals(expected, actual);
    }

    /**
     * Input was added to canvas; Thursday 20170810
     */
    @Ignore
    public void testNodes_7_OutTree() {
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + MILESTONE_1_INPUT + "Nodes_7_OutTree.dot");
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        String actual = GraphExporter.exportGraphToString(graph);
        String expected = FileUtils.readFileToString(TEST_FILES_PATH + MILESTONE_1_OUTPUT + "output_Nodes_7_OutTree.dot");

        assertEquals(expected, actual);
    }

    /**
     * This test ensures multiple cores are being used when they are required for the optimal schedule as there are no
     * dependencies between nodes.
     */
    @Ignore
    public void test8Nodes0Edges() {
        PROCESSOR_COUNT = 8; //8nodes/8cores
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + TEST_SOLVER + "input_8nodes_0edges.dot");
        solver = new AStarSolver(graph, PROCESSOR_COUNT); // Must construct solver only after graph has been parsed in.
        solver.doSolve();

        String actual = GraphExporter.exportGraphToString(graph);
        String expected = FileUtils.readFileToString(TEST_FILES_PATH + TEST_SOLVER + "output_8nodes_0edges.dot");
        System.out.println(actual);
        assertEquals(expected, actual);
    }

    /**
     * Tests for Milestone 1 input provided on canvas.
     * Processors = 2.
     */

    @Test
    public void testMilestone1Nodes7Processors2(){
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot"));
        graph.finalise();
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(28, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes8Processors2(){
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot"));
        graph.finalise();
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(581, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes9Processors2(){
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot"));
        graph.finalise();
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(55, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes10Processors2(){
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot"));
        graph.finalise();
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(50, solver.getFinalTime()); // test final time
    }

    // TODO Takes a while, pretty sure it's stuck
    @Test
    public void testMilestone1Nodes11Processors2(){
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot"));
        graph.finalise();
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
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot"));
        graph.finalise();
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(22, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes8Processors4(){
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot"));
        graph.finalise();
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(581, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes9Processors4(){
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot"));
        graph.finalise();
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(55, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes10Processors4(){
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot"));
        graph.finalise();
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(50, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes11Processors4(){
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot"));
        graph.finalise();
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(227, solver.getFinalTime()); // test final time
    }

}
