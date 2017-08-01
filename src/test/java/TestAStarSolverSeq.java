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

import java.io.*;

import static junit.framework.TestCase.assertEquals;

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
    public void setup() {
        graph = new Graph<Vertex, EdgeWithCost<Vertex>>();
        parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
    }

    private String exportGraphToString() {
        StringWriter stringWriter = new StringWriter();
        new GraphExporter<Vertex, EdgeWithCost<Vertex>>().doExport(graph, new BufferedWriter(stringWriter));
        return stringWriter.toString();
    }

    private String readFileToString(String fileName){
        StringBuilder output = new StringBuilder();
        try {
            String input;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(TEST_FILES_PATH+fileName));
            while ((input = bufferedReader.readLine()) != null){
                output.append(input);
                output.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }


    @Test
    public void testStraightLine(){
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH+"input_straightline.dot");
        solver = new AStarSolver(graph, PROCESSOR_COUNT);
        solver.doSolve();

        String actual = exportGraphToString();
        String expected = readFileToString("output_straightline.dot");
        assertEquals(expected, actual);
    }

}
