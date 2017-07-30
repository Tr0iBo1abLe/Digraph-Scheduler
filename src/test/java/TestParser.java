import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
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

    @Test
    public void testNodesBeforeEdges(){
        doParse("nodesBeforeEdges.dot");

        Set<Vertex> actualVertices = graph.getVertices();
        Set<Vertex> expectedVertices = new HashSet();
        expectedVertices.add(new Vertex("a", 2));
        expectedVertices.add(new Vertex("b", 3));
        expectedVertices.add(new Vertex("c", 3));
        expectedVertices.add(new Vertex("d", 2));
        assertEquals(expectedVertices, actualVertices);

        Set<EdgeWithCost<Vertex>> actualFrom = graph.getForwardEdges();
        Set<EdgeWithCost<Vertex>> expectedFrom = new HashSet<EdgeWithCost<Vertex>>();
        expectedFrom.add(new EdgeWithCost<Vertex>(new Vertex("a",2),new Vertex("b",3),1));
        expectedFrom.add(new EdgeWithCost<Vertex>(new Vertex("a",2),new Vertex("b",3),1));
        expectedFrom.add(new EdgeWithCost<Vertex>(new Vertex("a",2),new Vertex("b",3),1));
        expectedFrom.add(new EdgeWithCost<Vertex>(new Vertex("a",2),new Vertex("b",3),1));
        assertEquals(expectedFrom, actualFrom);

    }

}
