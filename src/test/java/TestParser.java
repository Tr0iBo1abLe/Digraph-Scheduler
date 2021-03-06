import Graph.Edge;
import Graph.EdgeWithCost;
import Graph.Exceptions.GraphException;
import Graph.Graph;
import Graph.Vertex;
import Parser.EdgeCtor;
import Parser.InputParser;
import Parser.VertexCtor;
import fj.data.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Unit tests for the input parser.
 * Tests that the data structures (Graph,Vertex and Edge) store the correct data for various input orders/types.
 * <p>
 * Created by will on 7/29/17.
 */
public class TestParser {


    private static final String TEST_FILES_PATH = "src/test/resources/TestParser/";
    private Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private InputParser<Vertex, EdgeWithCost<Vertex>> parser;

    private Vertex[] vertices;

    @Before
    public void setup() {
        graph = new Graph<Vertex, EdgeWithCost<Vertex>>();
        parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
    }

    /*
     * Destroy any testing objects.
     */
    @After
    public void cleanup() {
        vertices = null;
    }

    /**
     * Three tests for a simple input with all Vertices being parsed before Edges.
     * <p>
     * Makes sure Edges correspond to existing Vertex structures and their costs are correct.
     */
    private void nodesBeforeEdgesSetup() {
        // Parse file to graph
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + "verticesBeforeEdges.dot");

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
    public void testNodesBeforeEdgesVertexSet() {
        nodesBeforeEdgesSetup();

        Set<Vertex> actualVertices = graph.getVertices();
        Set<Vertex> expectedVertices = new HashSet<Vertex>();
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
    public void testNodesBeforeEdgesForwardEdgeSet() {
        nodesBeforeEdgesSetup();

        Set<EdgeWithCost<Vertex>> actualFwd = graph.getForwardEdges();
        Set<EdgeWithCost<Vertex>> expectedFwd = new HashSet<EdgeWithCost<Vertex>>();
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[0], vertices[1], 1));
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[0], vertices[2], 2));
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[1], vertices[3], 2));
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[2], vertices[3], 1));
        assertEquals(expectedFwd, actualFwd);
    }

    /**
     * Three tests for a simple input with all Edges before Vertices.
     * <p>
     * When edges are read in first temporary vertices are created. These tests ensures those vertices have their
     * costs updated once the corresponding vertices are read in.
     */
    private void verticesBeforeEdgesSetup() {
        // Parse file to graph
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + "edgesBeforeVertices.dot");

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
    public void testVerticesBeforeEdgesVertexSet() {
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
    public void testVerticesBeforeEdgesFwdEdgeSet() {
        verticesBeforeEdgesSetup();

        Set<EdgeWithCost<Vertex>> actualFwd = graph.getForwardEdges();
        Set<EdgeWithCost<Vertex>> expectedFwd = new HashSet<EdgeWithCost<Vertex>>();
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[0], vertices[1], 1));
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[0], vertices[2], 2));
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[1], vertices[3], 2));
        expectedFwd.add(new EdgeWithCost<Vertex>(vertices[2], vertices[3], 1));
        assertEquals(expectedFwd, actualFwd);
    }

    /**
     * Ensures Graph.getForwardVertices returns the set of "To" vertices for a given vertex.
     * The order doesn't matter.
     * <p>
     * In this case the graph looks like:
     * b <-- a --> c
     */
    @Test
    public void testGetForwardVertices() {
        vertices = new Vertex[3];
        vertices[0] = new Vertex("a", 2);
        vertices[1] = new Vertex("b", 3);
        vertices[2] = new Vertex("c", 3);

        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + "sampleinput.dot");

        List<Vertex> actual = graph.getChildrenVertices(vertices[0]);

        Set<Vertex> expectedSetup = new HashSet<Vertex>();
        expectedSetup.add(vertices[1]);
        expectedSetup.add(vertices[2]);
        List<Vertex> expected = fj.data.List.iterableList(expectedSetup);

        assertEquals(expected, actual);
    }

    /**
     * Ensures Graph.getReverseVertices returns the set of "From" vertices for a given vertex.
     * The order doesn't matter.
     * <p>
     * In this case the graph looks like:
     * b --> d <-- c
     */
    @Test
    public void testGetReverseVertices() {
        vertices = new Vertex[3];
        vertices[0] = new Vertex("d", 2);
        vertices[1] = new Vertex("b", 3);
        vertices[2] = new Vertex("c", 3);

        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + "sampleinput.dot");

        List<Vertex> actual = graph.getParentVertices(vertices[0]);

        ArrayList<Vertex> expectedSetup = new ArrayList<Vertex>();
        expectedSetup.add(vertices[2]);
        expectedSetup.add(vertices[1]);
        List<Vertex> expected = fj.data.List.iterableList(expectedSetup);

        assertEquals(expected, actual);
    }

    /**
     * Ensures getForwardEdge returns the correct edge (from: a, to: b, cost: 1). (a -(1)-> b)
     */
    @Test
    public void testGetForwardEdge() {
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + "sampleinput.dot");

        Vertex a = new Vertex("a", 2);
        Vertex b = new Vertex("b", 3);
        Edge edgeExpected = new EdgeWithCost(a, b, 1);
        Edge edgeActual = graph.getForwardEdge(a, b);

        assertEquals(edgeExpected, edgeActual);
    }


    /**
     * Ensures null is returned when looking up a vertex that doesn't exist in the graph.
     */
    @Test
    public void testLookupVertexByIDVertexDoesntExist() {
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + "sampleinput.dot");
        // Graph has a,b,c,d NOT z
        assertNull(graph.lookUpVertexById("z"));
    }

    /**
     * Ensures the correct vertex is returned (correct attributes etc)
     * when looking up a vertex that exists in the graph.
     */
    @Test
    public void testLookupVertexByIDVertexExists() {
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + "sampleinput.dot");
        Vertex aActual = graph.lookUpVertexById("a");
        Vertex aExpected = new Vertex("a", 2);
        assertEquals(aExpected, aActual);
    }

    /**
     * Ensures an Exception is thrown, with the correct message, when attempting to schedule a Vertex
     * that doesn't exist in the graph.
     */
    @Test
    public void testScheduleVertexNonExistingException() {
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + "sampleinput.dot");
        try {
            // Graph has a,b,c,d NOT z
            graph.scheduleVertex(new Vertex("z", 2), 0, 0);
            fail();
        } catch (GraphException e) {
            assertEquals("Attempting to schedule a non-existing vertex", e.getMessage());
        }
    }

    /**
     * Ensures a Scheduled vertex has had its processor and start time values set correctly.
     */
    @Test
    public void testScheduleVertexUpdatesCorrectly() {
        graph = parser.doParseAndFinaliseGraph(TEST_FILES_PATH + "sampleinput.dot");
        Vertex a = graph.lookUpVertexById("a");
        try {
            graph.scheduleVertex(a, 0, 0);
        } catch (GraphException e) {
            e.printStackTrace();
        }
        assertEquals(a.getProcessor(), 0);
        assertEquals(a.getStartTime(), 0);
    }


}
