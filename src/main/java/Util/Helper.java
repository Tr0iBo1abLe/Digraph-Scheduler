package Util;

import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import lombok.NonNull;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;

import java.util.stream.IntStream;


/**
 * Created by e on 2/08/17.
 */
public class Helper {
    public static void finalise(org.graphstream.graph.Graph g) {
        g.getNodeSet().forEach(v -> calculateBottomLevels(v, 0));
    }

    public static void calculateBottomLevels(@NonNull final Node v,
                                             final double level) {
        Double res;
        res = v.getAttribute("BL", Double.class);
        if(res == null) {
            v.setAttribute("BL", 0d);
            res = 0d;
        }
        if(res < level) {
            v.setAttribute("BL", level);
        }
        else {
            v.getEnteringEdgeSet().forEach(e -> {
                Node n = e.getSourceNode();
                calculateBottomLevels(n, level + v.getAttribute("Weight", Double.class));
            });
        }
    }

    public static void stripUneeded(@NonNull final org.graphstream.graph.Graph g) {
        g.getNodeSet().forEach(e -> {
            e.removeAttribute("BL");
        });
    }

    public static org.graphstream.graph.Graph convertToGsGraph(Graph<? extends Vertex, ? extends EdgeWithCost> convertee) {
        org.graphstream.graph.Graph newGraph = new DefaultGraph("g");
        int size = convertee.getVertices().size();
        IntStream.range(0, size).sequential().forEach(e -> {
            // We need to ensure the vertices have the same index(AssignedID)
            Vertex v = convertee.getVertex(e);
            Node n = newGraph.addNode(v.getId());
            n.addAttribute("Weight", v.getCost());
        });
        convertee.getForwardEdges().forEach(e -> {
            Vertex from, to;
            from = e.getFrom();
            to = e.getTo();
            org.graphstream.graph.Edge newE = newGraph.addEdge(from.getId() + to.getId(), from.getAssignedId(), to.getAssignedId());
            newE.addAttribute("Weight", e.getCost());
        });

        return newGraph;
    }
}
