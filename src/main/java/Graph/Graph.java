package Graph;

import Parser.IVertexCtor;
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

    public List<V> getForwardVertices(@NonNull final V v) {
        Set<E> es = forwardEdge.stream().filter(e -> e.getFrom().equals(v)).collect(Collectors.toSet());
        return es.stream().map(e -> e.getTo()).collect(Collectors.toList());
    }

    public List<V> getReverseVertices(@NonNull final V v) {
        Set<E> es = forwardEdge.stream().filter(e -> e.getTo().equals(v)).collect(Collectors.toSet());
        return es.stream().map(e -> e.getFrom()).collect(Collectors.toList());
    }

    public V lookUpVertexById(@NonNull final String id) {
        List<V> vs = vertices.stream().filter(i -> i.getId().equals(id)).collect(Collectors.toList());
        if(vs.size() != 1) {
            return null;
        }
        return vs.get(0);
    }

    public V ensureVertex(@NonNull final String id, @NonNull final IVertexCtor<V> ctor) {
        V res = lookUpVertexById(id);
        if(res == null) {
            // Vertex does not exist yet
            V newVertex = ctor.makeVertex(id);
            this.addVertex(newVertex);
            return newVertex;
        }
        else return res;
    }

    @Override
    public String toString() {
        String a = vertices.toString();
        String b = forwardEdge.toString();
        return a.concat(b);
    }
}
