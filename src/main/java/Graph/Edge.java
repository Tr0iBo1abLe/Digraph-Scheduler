package Graph;

import Graph.Interfaces.ICollectibleAttribute;
import Graph.Interfaces.IEdge;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ToString
@EqualsAndHashCode
public class Edge<V extends Vertex> implements IEdge<V>, ICollectibleAttribute {
    @NonNull
    protected final V from;
    @NonNull
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
    public Map<String, String> getAttributes() {
        return new LinkedHashMap<>();
    }

}
