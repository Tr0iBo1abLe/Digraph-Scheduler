package Graph;

import Graph.Interfaces.ICollectibleAttribute;
import Graph.Interfaces.IEdge;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Edge<V extends Vertex> implements IEdge<V>, ICollectibleAttribute {
    protected final V from;
    protected final V to;

    @Override
    public V getFromVertex() {
        return getFrom();
    }

    @Override
    public V getToVertex() {
        return getTo();
    }

    @Override
    public boolean equals(Object o) {
        Edge<V> e = (Edge<V>) o;
        return this.from.equals(e.from) && this.to.equals(e.to);
    }

    @Override
    public Map<String, String> getAttributes() {
        return new LinkedHashMap<>();
    }

    @Override
    public int hashCode(){
        return 1;
    }
}
