package Graph;

import Graph.Exceptions.GraphException;
import Graph.Exceptions.UncheckedException;
import Parser.Interfaces.IVertexCtor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;

import java.util.*;
import java.util.stream.Collectors;

public class Graph<V extends Vertex, E extends Edge<V>> {
    @Getter @Setter
    private String name;
    @Getter
    private Set<V> vertices;
    @Getter
    private Set<E> forwardEdges;
    @Getter
    private Set<Edge<V>> reverseEdges;
    @Getter
    private List<Object> order;

    public Graph() {
        this.vertices = new HashSet<>();
        this.forwardEdges = new HashSet<>();
        this.reverseEdges = new HashSet<>();
        this.order = new LinkedList<>();
    }

    public void addVertex(@NonNull final V v) {
        if(v.getCost() != 0) {
            order.add(v);
        }
        this.vertices.add(v);
    }

    public void addEdge(@NonNull final E e) throws GraphException {
        if(!vertices.contains(e.getFrom()) || !vertices.contains(e.getTo())) {
            throw new GraphException("Non existing vertex is being added to the graph." +
                    " Use ensureVertex() to to ensure it exists.");
        }
        this.forwardEdges.add(e);
        this.reverseEdges.add(new Edge<V>(e.getToVertex(), e.getFromVertex()));
        this.order.add(e);
    }

    /**
     * Returns the list of vertices that point to the given Vertex.
     */
    public List<V> getForwardVertices(@NonNull final V v) {
        Set<E> es = forwardEdges.stream().filter(e -> e.getFrom().equals(v)).collect(Collectors.toSet());
        return es.stream().map(e -> e.getTo()).collect(Collectors.toList());
    }

    /**
     * Returns the list of vertices that the given Vertex points to.
     */
    public List<V> getReverseVertices(@NonNull final V v) {
        Set<E> es = forwardEdges.stream().filter(e -> e.getTo().equals(v)).collect(Collectors.toSet());
        return es.stream().map(e -> e.getFrom()).collect(Collectors.toList());
    }

    /**
     * Returns the Vertex with the given ID. Asserts that only one vertex exists with that ID.
     */
    public V lookUpVertexById(final int id) {
        List<V> vs = vertices.stream().filter(i -> i.getAssignedId() == id).collect(Collectors.toList());
        if(vs.size() != 1) {
            return null; // TODO don't return null? Throw exception?
        }
        return vs.get(0);
        /**
         * TODO: Very inefficient, we will need to change the data structure to a Map later on...
         * For testing purpose, we will leave it here for now
         */
    }

    /**
     * Returns the Vertex with the given ID. Asserts that only one vertex exists with that ID.
     */
    public V lookUpVertexById(@NonNull final String id) {
        List<V> vs = vertices.stream().filter(i -> i.getId().equals(id)).collect(Collectors.toList());
        if(vs.size() != 1) {
            return null;
        }
        return vs.get(0);
    }

    /**
     * Ensure a Vertex with the id exists in the graph, if the vertex exists, return it,
     * otherwise, create a new one and return it.
     * @param id the id of the vertex
     * @param ctor the constructor of the vertex
     * @return the ensured vertex
     * @see IVertexCtor
     * @see Parser.VertexCtor
     */
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

    public void scheduleVertex(@NonNull final V v,
                               final int processor,
                               final int startTime) throws GraphException {
        if(!vertices.contains(v)) {
            throw new GraphException("Attempting to schedule a non-existing vertex");
        }
        else {
            v.setProcessor(processor);
            v.setStartTime(startTime);
            this.vertices.add(v);
        }
    }

    public E getForwardEdge(@NonNull final V from,
                            @NonNull final V to) {
        for(E e : forwardEdges) {
            if(e.getFrom().equals(from) && e.getTo().equals(to)) {
                return e;
            }
        }
        return null;
    }

    public Edge<V> getReverseEdge(@NonNull final V from,
                            @NonNull final V to) {
        for(Edge<V> e : reverseEdges) {
            if(e.getFrom().equals(from) && e.getTo().equals(to)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String a = vertices.toString();
        String b = forwardEdges.toString();
        return a.concat(b);
    }

    /**
     * Finalise the graph and fill in necessary information.
     */
    public void finalise() {
        for(V v : getVertices()) {
            calculateBottomLevels(v, 0);
        }
        assignIds();
    }

    /**
     * Assign unique numeric id to the vertices
     */
    @Synchronized
    private void assignIds() {
        int i = 0;
        for(V v : getVertices()) {
            v.setAssignedId(i);
            i++;
        }
    }

    /**
     * Exhaustively and recursively computed the bottom level for all the vertices.
     * @param v the vertex to compute
     * @param level the current level
     */
    private void calculateBottomLevels(@NonNull final V v,
                                       final int level) {
        if(v.getBottomLevel() < level) {
            v.setBottomLevel(level);
        }
        else {
            getForwardVertices(v).forEach(
                    w -> calculateBottomLevels(w, level + v.getCost()));
        }
    }

}
