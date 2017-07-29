package Parser;

import Graph.Exceptions.GraphException;
import Graph.Vertex;
import Parser.Interfaces.IVertexCtor;

import java.util.Map;

/**
 * Created by edward on 7/28/17.
 */
public class VertexCtor implements IVertexCtor<Vertex> {
    @Override
    public Vertex makeVertex(String id, Map<String, String> attrs) throws GraphException {
        String weight = attrs.get("Weight");
        if(weight == null)
            throw new GraphException("Vertex has no cost");
        return new Vertex(id, Integer.parseInt(attrs.getOrDefault("Weight", "0")));
    }

    @Override
    public Vertex makeVertex(String id) {
        return new Vertex(id, 0);
    }
}
