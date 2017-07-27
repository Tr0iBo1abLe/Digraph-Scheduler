package Graph;

import lombok.Getter;

import java.util.*;

public class Graph<V, E> {
    @Getter
    private Set<V> vertices;
    @Getter
    private Set<E> forwardEdge;
    @Getter
    private Set<E> reverseEdge;
    @Getter
    private List<Either<Vertex, Edge>> order;

    public Graph() {
        this.vertices = new LinkedHashSet<>();
        this.forwardEdge = new HashSet<>();
        this.reverseEdge = new HashSet<>();
    }


}
