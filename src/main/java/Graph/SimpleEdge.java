package Graph;

import Graph.Interfaces.ICollectibleAttribute;
import Graph.Interfaces.IEdge;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class SimpleEdge<V extends Vertex> implements IEdge<V>, ICollectibleAttribute {
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
        SimpleEdge<V> e = (SimpleEdge<V>) o;
        return this.from.equals(e.from) && this.to.equals(e.to);
    }

    @Override
    public Map<String, String> getAttributes() {
        return new LinkedHashMap<>();
    }
}
