import CommonInterface.ISolver;
import Exporter.GraphExporter;
import FileUtilities.FileUtils;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Parser.EdgeCtor;
import Parser.InputParser;
import Parser.VertexCtor;
import SolverOld.AStarSolver;
import org.junit.Before;
import org.junit.Test;


import static junit.framework.TestCase.assertEquals;

/**
 * Unit tests for the A* Solver implementation that's sequential (no parallel programming is tested here).
 *
 * This tests a possible solution for Milestone1 where we need a valid schedule (doesn't have to be optimal) and
 * doesn't need to be parallel.
 *
 * Created by will on 7/31/17.
 */
public class TestAStarSolverSeqOld_Milestone1 {

    private static final int PROCESSOR_COUNT = 2;
    private static final String TEST_FILES_PATH = "src/test/resources/",
            TEST_SOLVER = "TestSolver/",
            MILESTONE_1_INPUT = "input-graphs-milestone1/",
            MILESTONE_1_OUTPUT = "output-graphs-milestone1/";
    private Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private InputParser<Vertex, EdgeWithCost<Vertex>> parser;
    private ISolver solver;

    @Before
    public void setup() {
        graph = new Graph<Vertex, EdgeWithCost<Vertex>>();
        parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
    }

    @Test
    public void testStraightLine(){
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + TEST_SOLVER + "input_straightline_4nodes.dot");
        solver = new AStarSolver(graph, PROCESSOR_COUNT); // Must construct solver only after graph has been parsed in.
        solver.doSolve();

        String actual = GraphExporter.exportGraphToString(graph);
        String expected = FileUtils.readFileToString(TEST_FILES_PATH + TEST_SOLVER + "output_straightline_4nodes.dot");
        assertEquals(expected, actual);
    }

    @Test
    public void testNodes_7_OutTree() {
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + MILESTONE_1_INPUT + "Nodes_7_OutTree.dot");
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        String actual = GraphExporter.exportGraphToString(graph);
        String expected = FileUtils.readFileToString(TEST_FILES_PATH + MILESTONE_1_OUTPUT + "output_Nodes_7_OutTree.dot");
        System.out.println(actual);

        assertEquals(expected, actual);
    }

}
