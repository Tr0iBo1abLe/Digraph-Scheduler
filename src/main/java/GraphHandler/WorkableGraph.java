package GraphHandler;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.AsWeightedDirectedGraph;

import java.util.Map;

class WorkableGraph extends AsWeightedDirectedGraph{

    public WorkableGraph(DirectedGraph g, Map weightMap) {
        super(g, weightMap);
    }
}
