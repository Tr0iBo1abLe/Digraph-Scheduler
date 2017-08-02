import Exporter.GraphExporter;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Parser.EdgeCtor;
import Parser.InputParser;
import Parser.VertexCtor;
import Solver.AStarSolver;
import Solver.AStarSolverPar;
import Solver.Interfaces.ISolver;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

/**
 * Created by e on 1/08/17.
 */
public class TestPerformance {

    private static final int PROCESSOR_COUNT = 2;
    private static final String TEST_FILES_PATH = "src/test/resources/TestSolver/";
    private Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private InputParser<Vertex, EdgeWithCost<Vertex>> parser;

    @Before
    public void setup(){
        graph = new Graph<Vertex, EdgeWithCost<Vertex>>();
        parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
    }

    @Test
    public void testSeq(){
        long t = System.currentTimeMillis();
        ISolver solver;
        graph = parser.doParseAndFinaliseGraph("input3.dot");
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();
        final GraphExporter<Vertex,EdgeWithCost<Vertex>> vertexEdgeWithCostGraphExporter;
        vertexEdgeWithCostGraphExporter = new GraphExporter<Vertex, EdgeWithCost<Vertex>>();
        vertexEdgeWithCostGraphExporter.doExport(graph, new BufferedWriter(new OutputStreamWriter(System.out)));
        long delta = System.currentTimeMillis() - t;
        System.out.println("Seq Astar Time taken = " + delta);
    }

    @Test
    public void testPar(){
        long t = System.currentTimeMillis();
        ISolver solver;
        graph = parser.doParseAndFinaliseGraph("input3.dot");
        solver = new AStarSolverPar(graph, PROCESSOR_COUNT);
        solver.doSolve();
        final GraphExporter<Vertex,EdgeWithCost<Vertex>> vertexEdgeWithCostGraphExporter;
        vertexEdgeWithCostGraphExporter = new GraphExporter<Vertex, EdgeWithCost<Vertex>>();
        vertexEdgeWithCostGraphExporter.doExport(graph, new BufferedWriter(new OutputStreamWriter(System.out)));
        long delta = System.currentTimeMillis() - t;
        System.out.println("Par Astar Time taken = " + delta);
    }

}
