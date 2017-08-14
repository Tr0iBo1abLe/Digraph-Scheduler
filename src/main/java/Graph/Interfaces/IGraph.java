package Graph.Interfaces;


import Graph.Exceptions.GraphException;
import fj.data.List;

/**
 * Created by e on 6/08/17.
 */
public interface IGraph<V, E> {
    void addVertex(V v);

    void addEdge(E e) throws GraphException;

    List<V> getChildrenVertices(V v);

    List<V> getParentVertices(V v);

    V getVertex(int index);

    V getVertex(String id);

    List<E> getInwardsEdges(V v);

    List<E> getOutwardsEdges(V v);

}
