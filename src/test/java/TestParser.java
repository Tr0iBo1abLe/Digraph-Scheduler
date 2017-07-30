import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Graph.Edge;
import Parser.EdgeCtor;
import Parser.Exceptions.ParserException;
import Parser.InputParser;
import Parser.VertexCtor;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the input parser.
 *
 * Created by will on 7/29/17.
 */
public class TestParser {

    private static final String FILE_PATH = "src/test/resources/";
    private Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private InputParser<Vertex, EdgeWithCost<Vertex>> parser;

    @Before
    public void setup(){
        graph = new Graph<Vertex, EdgeWithCost<Vertex>>();
        parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
    }

    private void doParse(String file){
        try {
            parser.doParse(graph, new BufferedReader(new FileReader(FILE_PATH + file)));
        } catch (ParserException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests a simple input with all Vertices being parsed before Edges.
     */
    @Test
    public void testVerticesBeforeEdges(){
        doParse("nodesBeforeEdges.dot");

        Vertex a2 = new Vertex("a", 2);
        Vertex b3 = new Vertex("b", 3);
        Vertex c3 = new Vertex("c", 3);
        Vertex d2 = new Vertex("d", 2);

        // Test set of stored Vertices is correct.
        Set<Vertex> actualVertices = graph.getVertices();
        Set<Vertex> expectedVertices = new HashSet();
        expectedVertices.add(a2);
        expectedVertices.add(b3);
        expectedVertices.add(c3);
        expectedVertices.add(d2);
        assertEquals(expectedVertices, actualVertices);

        // Test set of ForwardEdges is correct; note they include the edge cost.
        Set<EdgeWithCost<Vertex>> actualFwd = graph.getForwardEdges();
        Set<EdgeWithCost<Vertex>> expectedFwd = new HashSet<EdgeWithCost<Vertex>>();
        expectedFwd.add(new EdgeWithCost<Vertex>(a2, b3,1));
        expectedFwd.add(new EdgeWithCost<Vertex>(a2, c3,2));
        expectedFwd.add(new EdgeWithCost<Vertex>(b3, d2,2));
        expectedFwd.add(new EdgeWithCost<Vertex>(c3, d2,1));
        assertEquals(expectedFwd, actualFwd);

        // Test set of ReverseEdges is correct; note they don't store the cost.
        Set<Edge<Vertex>> actualReverse = graph.getReverseEdges();
        Set<Edge<Vertex>> expectedReverse = new HashSet<Edge<Vertex>>();
        expectedReverse.add(new Edge<Vertex>(b3, a2));
        expectedReverse.add(new Edge<Vertex>(c3, a2));
        expectedReverse.add(new Edge<Vertex>(d2, b3));
        expectedReverse.add(new Edge<Vertex>(d2, c3));
        assertEquals(expectedReverse, actualReverse);
    }

}
