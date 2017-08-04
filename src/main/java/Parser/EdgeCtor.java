package Parser;

import Graph.EdgeWithCost;
import Graph.Vertex;
import Parser.Interfaces.IEdgeCtor;

import java.util.Map;

/**
 * Created by edward on 7/28/17.
 */
public class EdgeCtor implements IEdgeCtor<Vertex, EdgeWithCost<Vertex>> {
    @Override
    public EdgeWithCost<Vertex> makeEdge(Vertex vFrom, Vertex vTo, Map<String, String> attrs) {
        return new EdgeWithCost<Vertex>(vFrom, vTo, Integer.parseInt(attrs.getOrDefault("Weight", "0")));
    }
}
