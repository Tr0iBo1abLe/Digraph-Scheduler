package Graph;

import Graph.Exceptions.GraphException;
import Graph.Interfaces.IGraph;
import Parser.Interfaces.IVertexCtor;
import lombok.*;
import lombok.experimental.NonFinal;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A graph, finalise() must be called before formal query can be done
 * @param <V> The Vertex type
 * @param <E> The Edge type
 */
@Value
public class Graph<V extends Vertex, E extends Edge<V>> implements IGraph<V, E> {
    @NonFinal @Setter
    private String name = null;
    private Set<V> vertices;
    private Set<E> forwardEdges;
    private List<Object> order;
    private Map<Integer, V> verticesMap;

    /* FJ provides efficient immutable List */
    private HashMap<V, fj.data.List<E>> inwardEdgeMap;
    private HashMap<V, fj.data.List<E>> outwardEdgeMap;
    private HashMap<V, fj.data.List<V>> parentVertexMap;
    private HashMap<V, fj.data.List<V>> childrenVertexMap;

    /**
     * Construct a new empty graph
     */
    public Graph() {
        this.vertices = new HashSet<>();
        this.forwardEdges = new HashSet<>();
        this.order = new LinkedList<>();

        this.verticesMap = new HashMap<>();
        this.inwardEdgeMap = new HashMap<>();
        this.outwardEdgeMap = new HashMap<>();
        this.parentVertexMap = new HashMap<>();
        this.childrenVertexMap = new HashMap<>();
    }

    /**
     * Add a vertex to the graph
     * @param v the vertex to add
     */
    @Override
    public void addVertex(@NonNull final V v) {
        if (v.getCost() != 0) {
            order.add(v);
        }
        this.vertices.add(v);
    }

    /**
     * Add an edge to the graph, the vertex being referenced must already be in the graph
     * @param e the edge to add
     * @throws GraphException when any vertex does not exist in the graph
     * @see ensureVertex
     */
    @Override
    public void addEdge(@NonNull final E e) throws GraphException {
        if (!vertices.contains(e.getFrom()) || !vertices.contains(e.getTo())) {
            throw new GraphException("Non existing vertex is being added to the graph." +
                    " Use ensureVertex() to ensure it exists.");
        }
        this.forwardEdges.add(e);
        this.order.add(e);
    }

    /**
     * Ensure a Vertex with the id exists in the graph, if the vertex exists, return it,
     * otherwise, create a new one and return it.
     *
     * @param id   the id of the vertex
     * @param ctor the constructor of the vertex
     * @return the ensured vertex
     * @see IVertexCtor
     * @see Parser.VertexCtor
     */
    public V ensureVertex(@NonNull final String id, @NonNull final IVertexCtor<V> ctor) {
        V res = getVertex(id);
        if (res == null) {
            // Vertex does not exist yet
            V newVertex = ctor.makeVertex(id);
            this.addVertex(newVertex);
            return newVertex;
        } else return res;
    }

    /**
     * Get the children of a vertex
     * @param v the vertex to get
     * @return a List of vertices
     */
    @Override
    public fj.data.List<V> getChildrenVertices(V v) {
        return childrenVertexMap.get(v);
    }

    /**
     * Get the parent of a vertex
     * @param v the vertex to get
     * @return a List of vertices
     */
    @Override
    public fj.data.List<V> getParentVertices(V v) {
        return parentVertexMap.get(v);
    }

    /**
     * Get a vertex by its assigned ID
     * @param index the assigned ID
     * @return the vertex. null if doesn't exist
     */
    @Override
    public V getVertex(int index) {
        return verticesMap.get(index);
    }

    /**
     * Get a vertex by its String ID
     * @param id the name of the vertex
     * @return the vertex. null if doesn't exist
     */
    @Override
    public V getVertex(String id) {
        List<V> vs = vertices.stream().filter(i -> i.getId().equals(id)).collect(Collectors.toList());
        if (vs.size() != 1) {
            return null;
        }
        return vs.get(0);
    }

    /**
     * Get the edges pointing out from a vertex
     * @param v the vertex
     * @return a list of edges
     */
    @Override
    public fj.data.List<E> getInwardsEdges(V v) {
        return inwardEdgeMap.get(v);
    }

    /**
     * Get the edges pointing in to a vertex
     * @param v the vertex
     * @return a list of edges
     */
    @Override
    public fj.data.List<E> getOutwardsEdges(V v) {
        return outwardEdgeMap.get(v);
    }

    /**
     * Method to get outward vertices
     * @param es
     * @return list for outward vertices
     */
    private List<V> getForwardVertices(@NonNull final Set<E> es) {
        return es.stream().map(e -> e.getTo()).collect(Collectors.toList());
    }

    /**
     * Method to get inward vertices
     * @param es
     * @return list for inward vertices
     */
    private List<V> getReverseVertices(@NonNull final Set<E> es) {
        return es.stream().map(e -> e.getFrom()).collect(Collectors.toList());
    }

    /**
     * Method to get inward edges for a vertex
     * O(N)
     * @param v vertex to get
     * @return list for inward vertices
     */
    private Set<E> getInwardEdges(@NonNull final V v) {
        return forwardEdges.stream().filter(e -> e.getTo().equals(v)).collect(Collectors.toSet());
    }
    /**
     * Method to get outwardward edges for a vertex
     * O(N)
     * @param v vertex to get
     * @return list for inward vertices
     */
    private Set<E> getOutwardEdges(@NonNull final V v) {
        return forwardEdges.stream().filter(e -> e.getFrom().equals(v)).collect(Collectors.toSet());
    }

    /**
     * Returns the Vertex with the given ID. Asserts that only one vertex exists with that ID.
     */
    @Deprecated
    public V lookUpVertexById(final int id) {
        return this.verticesMap.get(id);
    }

    /**
     * Returns the Vertex with the given ID. Asserts that only one vertex exists with that ID.
     */
    @Deprecated
    public V lookUpVertexById(@NonNull final String id) {
        List<V> vs = vertices.stream().filter(i -> i.getId().equals(id)).collect(Collectors.toList());
        if (vs.size() != 1) {
            return null;
        }
        return vs.get(0);
    }

    public void scheduleVertex(@NonNull final V v,
                               final int processor,
                               final int startTime) throws GraphException {
        if (!vertices.contains(v)) {
            throw new GraphException("Attempting to schedule a non-existing vertex");
        } else {
            v.setProcessor(processor);
            v.setStartTime(startTime);
            this.vertices.add(v);
        }
    }

    public E getForwardEdge(@NonNull final V from,
                            @NonNull final V to) {
        for (E e : forwardEdges) {
            if (e.getFrom().equals(from) && e.getTo().equals(to)) {
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
     * Must be called before the graph can be formally queried.
     */
    public void finalise() {
        assignIds();
        buildMaps();
        getVertices().forEach(vertex -> calculateBottomLevels(vertex, 0));
    }

    /**
     * Build internal representations
     */
    private void buildMaps() {
        getVertices().forEach(vertex -> {
            Set<E> inwards = getInwardEdges(vertex);
            Set<E> outwards = getOutwardEdges(vertex);
            this.inwardEdgeMap.put(vertex, fj.data.List.iterableList(inwards));
            this.outwardEdgeMap.put(vertex, fj.data.List.iterableList(outwards));
            this.parentVertexMap.put(vertex, fj.data.List.iterableList(getReverseVertices(inwards)));
            this.childrenVertexMap.put(vertex, fj.data.List.iterableList(getForwardVertices(outwards)));
        });
    }

    /**
     * Assign unique numeric id to the vertices
     */
    @Synchronized
    private void assignIds() {
        int i = 0;
        for (V vertex : getVertices()) {
            vertex.setAssignedId(i);
            this.verticesMap.put(i, vertex);
            i++;
        }
    }

    /**
     * Exhaustively and recursively compute the bottom level for all the vertices.
     *
     * @param vertex the vertex to compute
     * @param level the current level
     */
    private void calculateBottomLevels(@NonNull final V vertex,
                                       final int level) {
        if (vertex.getBottomLevel() < level) {
            vertex.setBottomLevel(level);
        } else {
            getParentVertices(vertex).forEach(parentVertex ->
                    calculateBottomLevels(parentVertex, level + vertex.getCost()));
        }
    }



}
