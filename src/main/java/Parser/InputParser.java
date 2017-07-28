package Parser;

import Graph.Graph;
import Graph.SimpleEdge;
import Graph.Vertex;
import lombok.NonNull;

import java.io.File;

/**
 * Created by edward on 7/27/17.
 */
public class InputParser<V extends Vertex, E extends SimpleEdge<V>> {

    private enum STATE {HEADER, BODY, VERTEX, EDGE, ATTRIBUTE};
    private IVertexCtor<V> vertexCtor;
    private IEdgeCtor<V, E> edgeCtor;

    public InputParser(@NonNull IVertexCtor<V> vertexCtor,
                       @NonNull IEdgeCtor<V, E> edgeCtor) {
        this.vertexCtor = vertexCtor;
        this.edgeCtor = edgeCtor;
    }

    public void doParse(Graph<V, E> graph, @NonNull File file) {

    }

}

