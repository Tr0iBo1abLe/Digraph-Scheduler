import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.AsWeightedDirectedGraph;

import java.util.Map;

public class Graph extends AsWeightedDirectedGraph{

    public Graph(DirectedGraph g, Map weightMap) {
        super(g, weightMap);
    }
}
