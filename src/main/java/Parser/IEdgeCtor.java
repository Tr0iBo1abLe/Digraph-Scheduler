package Parser;

import Graph.IEdge;

import java.util.Map;

/**
 * Created by e on 28/07/17.
 */
public interface IEdgeCtor<V, E extends IEdge> {
    public E makeEdge(V vFrom, V vTo, Map<String, String> attrs);
}
