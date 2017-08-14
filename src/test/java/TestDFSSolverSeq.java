import Exporter.GraphExporter;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Parser.EdgeCtor;
import Parser.InputParser;
import Parser.VertexCtor;
import Solver.AbstractSolver;
import Solver.DFSSolver;
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
 * Unit tests for the DFS Solver implementation that's sequential (no parallel programming is tested here).
 * <p>
 * Created by mason on 7/31/17.
 */
@Ignore
public class TestDFSSolverSeq {

    private static final String TEST_FILES_PATH = "src/test/resources/TestSolver/";
    private static int PROCESSOR_COUNT;
    private Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private InputParser<Vertex, EdgeWithCost<Vertex>> parser;
    private AbstractSolver solver;

    @Before
    public void setup() {
        PROCESSOR_COUNT = 2;
        graph = new Graph<Vertex, EdgeWithCost<Vertex>>();
        parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
    }

    @Test
    public void testStraightLine() {
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + "input_straightline_4nodes.dot");
        solver = new DFSSolver(graph, PROCESSOR_COUNT); // Must construct solver only after graph has been parsed in.
        solver.doSolve();

        String actual = GraphExporter.exportGraphToString(graph);
        String expected = FileUtils.readFileToString(TEST_FILES_PATH + "output_straightline_4nodes.dot");
        assertEquals(expected, actual);
    }

    /**
     * This test ensures multiple cores are being used when they are required for the optimal schedule as there are no
     * dependencies between nodes.
     */
    @Ignore
    public void test8Nodes0Edges() {
        PROCESSOR_COUNT = 8;
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + "input_8nodes_0edges.dot");
        solver = new DFSSolver(graph, PROCESSOR_COUNT); // Must construct solver only after graph has been parsed in.
        solver.doSolve();

        String actual = GraphExporter.exportGraphToString(graph);
        String expected = FileUtils.readFileToString(TEST_FILES_PATH + "output_8nodes_0edges.dot");
        System.out.println(actual);
        assertEquals(expected, actual);
    }


    /**
     * Tests for Milestone 1 input provided on canvas.
     * Processors = 2.
     */

    @Test
    public void testMilestone1Nodes7Processors2() {
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot"));
        graph.finalise();
        solver = new DFSSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(28, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes8Processors2() {
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot"));
        graph.finalise();
        solver = new DFSSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(581, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes9Processors2() {
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot"));
        graph.finalise();
        solver = new DFSSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(55, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes10Processors2() {
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot"));
        graph.finalise();
        solver = new DFSSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(50, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes11Processors2() {
        PROCESSOR_COUNT = 2;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot"));
        graph.finalise();
        solver = new DFSSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(350, solver.getFinalTime()); // test final time
    }

    /**
     * Tests for Milestone 1 input provided on canvas.
     * Processors = 4.
     */

    @Test
    public void testMilestone1Nodes7Processors4() {
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_7_OutTree.dot"));
        graph.finalise();
        solver = new DFSSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(22, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes8Processors4() {
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_8_Random.dot"));
        graph.finalise();
        solver = new DFSSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(581, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes9Processors4() {
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_9_SeriesParallel.dot"));
        graph.finalise();
        solver = new DFSSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(55, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes10Processors4() {
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_10_Random.dot"));
        graph.finalise();
        solver = new DFSSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(50, solver.getFinalTime()); // test final time
    }

    @Test
    public void testMilestone1Nodes11Processors4() {
        PROCESSOR_COUNT = 4;
        graph = Helper.fileToGraph(new File(TEST_FILE_PATH + TEST_MILESTONE_1_INPUT_PATH + "Nodes_11_OutTree.dot"));
        graph.finalise();
        solver = new DFSSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        assertEquals(227, solver.getFinalTime()); // test final time
    }

}
