import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Graph.Edge;
import Parser.EdgeCtor;
import Parser.Exceptions.ParserException;
import Parser.InputParser;
import Parser.VertexCtor;
import org.junit.After;
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
 * Tests that the data structures store the correct objects for various input orders/types.
 *
 * Created by will on 7/29/17.
 */
public class TestParser {

    private static final String FILE_PATH = "src/test/resources/";
    private Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private InputParser<Vertex, EdgeWithCost<Vertex>> parser;

    private Vertex[] vertices;

    @Before
    public void setup(){
        graph = new Graph<Vertex, EdgeWithCost<Vertex>>();
        parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
    }

    /*
     * Destroy any testing objects.
     */
    @After
    public void cleanup(){
        vertices = null;
    }

    private void doParse(String file){
        try {
            parser.doParse(graph, new BufferedReader(new FileReader(FILE_PATH + file)));
        } catch (ParserException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Three tests for a simple input with all Vertices being parsed before Edges.
     *
     * Makes sure Edges correspond to existing Vertex structures and their costs are correct.
     */
    private void nodesBeforeEdgesSetup(){
        // Parse file to graph
        doParse("nodesBeforeEdges.dot");

        // Create expected vertices
        vertices = new Vertex[4];
        vertices[0] = new Vertex("a", 2);
        vertices[1] = new Vertex("b", 3);
        vertices[2] = new Vertex("c", 3);
        vertices[3] = new Vertex("d", 2);
    }

    /**
     * Test set of stored Vertices is correct.
     */
    @Test
    public void testNodesBeforeEdgesVertexSet(){
        nodesBeforeEdgesSetup();

        Set<Vertex> actualVertices = graph.getVertices();
        Set<Vertex> expectedVertices = new HashSet();
        expectedVertices.add(vertices[0]);
        expectedVertices.add(vertices[1]);
        expectedVertices.add(vertices[2]);
        expectedVertices.add(vertices[3]);
        assertEquals(expectedVertices, actualVertices);
    }

    /**
     * Test set of ForwardEdges is correct; note they include the edge cost.
      */
    @Test
    public void testNodesBeforeEdgesForwardEdgeSet(){
        nodesBeforeEdgesSetup();

        Set<EdgeWithCost<Vertex>> actualFwd = graph.getForwardEdges();
        Set<EdgeWithCost<Vertex>> expectedFwd = new HashSet<EdgeWithCost<Vertex>>();
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[0], vertices[1],1));
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[0], vertices[2],2));
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[1], vertices[3],2));
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[2], vertices[3],1));
        assertEquals(expectedFwd, actualFwd);
    }

    /**
     * Test set of ReverseEdges is correct; note they don't store the cost.
     */
    @Test
    public void testNodesBeforeEdgesReverseEdgeSet(){
        nodesBeforeEdgesSetup();

        Set<Edge<Vertex>> actualReverse = graph.getReverseEdges();
        Set<Edge<Vertex>> expectedReverse = new HashSet<Edge<Vertex>>();
        expectedReverse.add(new Edge<Vertex>(vertices[1], vertices[0]));
        expectedReverse.add(new Edge<Vertex>(vertices[2], vertices[0]));
        expectedReverse.add(new Edge<Vertex>(vertices[3], vertices[1]));
        expectedReverse.add(new Edge<Vertex>(vertices[3], vertices[2]));
        assertEquals(expectedReverse, actualReverse);
    }

    /**
     * Three tests for a simple input with all Edges before Vertices.
     *
     * When edges are read in first temporary vertices are created. These tests ensures those vertices have their
     * costs updated once the corresponding vertices are read in.
     */
    private void verticesBeforeEdgesSetup(){
        // Parse file to graph
        doParse("edgesBeforeNodes.dot");

        // Create expected vertices
        vertices = new Vertex[4];
        vertices[0] = new Vertex("a", 2);
        vertices[1] = new Vertex("b", 3);
        vertices[2] = new Vertex("c", 3);
        vertices[3] = new Vertex("d", 2);
    }


    /**
     * Test set of stored Vertices is correct.
     */
    @Test
    public void testVerticesBeforeEdgesVertexSet(){
        verticesBeforeEdgesSetup();

        Set<Vertex> actualVertices = graph.getVertices();
        Set<Vertex> expectedVertices = new HashSet();
        expectedVertices.add(vertices[0]);
        expectedVertices.add(vertices[1]);
        expectedVertices.add(vertices[2]);
        expectedVertices.add(vertices[3]);
        assertEquals(expectedVertices, actualVertices);
    }

    /**
     * Test set of ForwardEdges is correct; note they include the edge cost.
     */
    @Test
    public void testVerticesBeforeEdgesFwdEdgeSet(){
        verticesBeforeEdgesSetup();

        Set<EdgeWithCost<Vertex>> actualFwd = graph.getForwardEdges();
        Set<EdgeWithCost<Vertex>> expectedFwd = new HashSet<EdgeWithCost<Vertex>>();
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[0], vertices[1],1));
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[0], vertices[2],2));
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[1], vertices[3],2));
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[2], vertices[3],1));
        assertEquals(expectedFwd, actualFwd);
    }

    /**
     * Test set of ReverseEdges is correct; note they don't store the cost.
     */
    @Test
    public void testVerticesBeforeEdgesRvrEdgeSet(){
        verticesBeforeEdgesSetup();

        Set<Edge<Vertex>> actualReverse = graph.getReverseEdges();
        Set<Edge<Vertex>> expectedReverse = new HashSet<Edge<Vertex>>();
        expectedReverse.add(new Edge<Vertex>(vertices[1], vertices[0]));
        expectedReverse.add(new Edge<Vertex>(vertices[2], vertices[0]));
        expectedReverse.add(new Edge<Vertex>(vertices[3], vertices[1]));
        expectedReverse.add(new Edge<Vertex>(vertices[3], vertices[2]));
        assertEquals(expectedReverse, actualReverse);
    }


}
