package test;

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

/**
 * Unit tests for the input parser.
 *
 * Created by will on 7/29/17.
 */
public class TestParser {

    private Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private InputParser<Vertex, EdgeWithCost<Vertex>> parser;

    @Before
    public void setup(){
        graph = new Graph<Vertex, EdgeWithCost<Vertex>>();
        parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
    }

    private void parserDoParse(String file){
        try {
            parser.doParse(graph, new BufferedReader(new FileReader(file)));
        } catch (ParserException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNodesBeforeEdges(){
        parserDoParse("nodesBeforeEdges.dot");

    }

}
