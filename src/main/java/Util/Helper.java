package Util;

import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Parser.EdgeCtor;
import Parser.InputParser;
import Parser.VertexCtor;
import lombok.experimental.UtilityClass;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

@UtilityClass
public class Helper {

    public org.graphstream.graph.Graph convertToGsGraph(Graph<? extends Vertex, ? extends EdgeWithCost> convertee) {
        org.graphstream.graph.Graph newGraph = new DefaultGraph("g");
        int size = convertee.getVertices().size();
        IntStream.range(0, size).sequential().forEach(e -> {
            // We need to ensure the vertices have the same index(AssignedID)
            Vertex v = convertee.getVertex(e);
            Node n = newGraph.addNode(v.getId());
            n.addAttribute("Weight", v.getCost());
            n.addAttribute("label", v.getId());
        });
        convertee.getForwardEdges().forEach(e -> {
            Vertex from, to;
            from = e.getFrom();
            to = e.getTo();
            org.graphstream.graph.Edge newE = newGraph.addEdge(from.getId() + to.getId(), from.getAssignedId(), to.getAssignedId(), true);
            newE.addAttribute("Weight", e.getCost());
        });

        return newGraph;
    }

    public Graph<Vertex, EdgeWithCost<Vertex>> fileToGraph(File inputFile) {
        Graph<Vertex, EdgeWithCost<Vertex>> graph;

        InputParser<Vertex, EdgeWithCost<Vertex>> parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
        graph = parser.doParseAndFinaliseGraph(inputFile);

        return graph;
    }

    public String gsGraphToDOTString(org.graphstream.graph.Graph graph) {
        FileSink sink = new FileSinkDOT();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            sink.writeAll(graph, os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os.toString();
    }

    public long getRemainingMemory() {
        long allocatedMemory =
                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        return Runtime.getRuntime().maxMemory() - allocatedMemory;
    }

}
