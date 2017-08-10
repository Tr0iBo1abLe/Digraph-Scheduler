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
import SolverOld.DFSSolver;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Unit tests for the DFS Solver implementation that's sequential (no parallel programming is tested here).
 *
 * Created by mason on 7/31/17.
 */
public class TestDFSSolverSeqOld {

    private static int PROCESSOR_COUNT;
    private static final String TEST_FILES_PATH = "src/test/resources/TestSolver/";
    private Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private InputParser<Vertex, EdgeWithCost<Vertex>> parser;
    private ISolver solver;

    @Before
    public void setup() {
        PROCESSOR_COUNT = 2;
        graph = new Graph<Vertex, EdgeWithCost<Vertex>>();
        parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
    }

    @Test
    public void testStraightLine(){
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH+"input_straightline_4nodes.dot");
        solver = new DFSSolver(graph, PROCESSOR_COUNT); // Must construct solver only after graph has been parsed in.
        solver.doSolve();

        String actual = GraphExporter.exportGraphToString(graph);
        String expected = FileUtils.readFileToString(TEST_FILES_PATH+"output_straightline_4nodes.dot");
        assertEquals(expected, actual);
    }

    /**
     * This test ensures multiple cores are being used when they are required for the optimal schedule as there are no
     * dependencies between nodes.
     */
    @Ignore
    public void test8Nodes0Edges(){
        PROCESSOR_COUNT = 5;
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + "input_8nodes_0edges.dot");
        solver = new DFSSolver(graph, PROCESSOR_COUNT); // Must construct solver only after graph has been parsed in.
        solver.doSolve();

        String actual = GraphExporter.exportGraphToString(graph);
        String expected = FileUtils.readFileToString(TEST_FILES_PATH + "output_8nodes_0edges.dot");
        System.out.println(actual);
        assertEquals(expected, actual);
    }


}
