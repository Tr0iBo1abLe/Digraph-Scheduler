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

    @Override
    public boolean equals(Object o) {
        SimpleEdge<V> e = (SimpleEdge<V>) o;
        return this.from.equals(e.from) && this.to.equals(e.to);
    }

}
