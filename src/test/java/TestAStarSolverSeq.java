import Exporter.GraphExporter;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Parser.EdgeCtor;
import Parser.InputParser;
import Parser.VertexCtor;
import Solver.AStarSolver;
import Solver.Interfaces.ISolver;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

/**
 * Unit tests for the A* Solver implementation that's sequential (no parallel programming is tested here).
 *
 * Created by will on 7/31/17.
 */
public class TestAStarSolverSeq {

    private static final int PROCESSOR_COUNT = 4;
    private static final String TEST_FILES_PATH = "src/test/resources/TestSolver/";
    private Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private InputParser<Vertex, EdgeWithCost<Vertex>> parser;
    private ISolver solver;

    @Before
    public void setup(){
        graph = new Graph<Vertex, EdgeWithCost<Vertex>>();
        parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
    }

    @Test
    public void testStraightLine(){
        graph = parser.doParse(TEST_FILES_PATH+"input_straightline.dot");
        graph.finalise();
        solver = new AStarSolver(graph, PROCESSOR_COUNT); // cant construct solver before graph.finalise() ..?
        solver.doSolve();
        // TODO read in expected output file for comparison with toString() of solved graph.
        final GraphExporter<Vertex,EdgeWithCost<Vertex>> vertexEdgeWithCostGraphExporter;
        vertexEdgeWithCostGraphExporter = new GraphExporter<Vertex, EdgeWithCost<Vertex>>();
        vertexEdgeWithCostGraphExporter.doExport(graph, new BufferedWriter(new OutputStreamWriter(System.out)));

    }

}
