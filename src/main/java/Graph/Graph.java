package Graph;

import lombok.Getter;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class Graph<V extends Vertex, E extends SimpleEdge<V>> {
    @Getter
    private Set<V> vertices;
    @Getter
    private Set<E> forwardEdge;
    @Getter
    private Set<SimpleEdge<V>> reverseEdge;
    @Getter
    private List<Either<Vertex, E>> order;

    public Graph() {
        this.vertices = new HashSet<>();
        this.forwardEdge = new HashSet<>();
        this.reverseEdge = new HashSet<>();
    }

    public void addVertex(@NonNull final V v) {
        this.vertices.add(v);
    }

    public void addEdge(@NonNull final E e) {
        this.forwardEdge.add(e);
        this.reverseEdge.add(new SimpleEdge<V>(e.getFromVertex(), e.getToVertex()));
    }

    public List<V> getForwardVertices(@NonNull V v) {
        Set<E> es = forwardEdge.stream().filter(e -> e.getFrom().equals(v)).collect(Collectors.toSet());
        return es.stream().map(e -> e.getTo()).collect(Collectors.toList());
    }

    public List<V> getReverseVertices(@NonNull V v) {
        Set<E> es = forwardEdge.stream().filter(e -> e.getTo().equals(v)).collect(Collectors.toSet());
        return es.stream().map(e -> e.getFrom()).collect(Collectors.toList());
    }

    public V lookUpVertexById(@NonNull V v) {
        V[] vs = (V[]) vertices.stream().filter(i -> i.getId().equals(v.getId())).toArray();
        if(vs.length != 1) {
            return null;
        }
        return vs[0];
    }


}
