package Graph;

import lombok.Data;

@Data
public class SimpleEdge<V> implements IEdge<V>{
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

}
