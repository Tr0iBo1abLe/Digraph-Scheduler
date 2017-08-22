package Solver;

import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import fj.data.List;
import fj.data.vector.V;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * DFS Solver, uses branch and bound technique to prune search states in the stack.
 * Doesn't use much memory since the current best state is cached while the stack is cleared on each iteration.
 *
 * Created by mason on 31/07/17.
 * @author Mason Shi, Edward Huang, Will Molloy
 */
@Data
public final class DFSSolver extends AbstractSolver {

    private int currentUpperBound;
    private SearchState result;

    public DFSSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
    }

    @Override
    public void doSolve() {
        SearchState.initialise(graph);
        // Upper bound is initially all the nodes scheduled to one processor (when edge cost can be ignored)
        currentUpperBound = topologicalSortSolve();
        if (processorCount != 1) { // when processorCount = 1, topologicalSort is the solution.
            SearchState s = new SearchState();
            solving(s);
        }
        scheduleVertices(result);
    }

    private void solving(SearchState s) {
        s.getLegalVertices().forEach(v -> IntStream.range(0, processorCount).forEach(i -> {
                    SearchState next = new SearchState(s, v, i);
                    if (next.getUnderestimate() >= currentUpperBound) {
                        return;
                    }
                    if (next.getNumVertices() == graph.getVertices().size()) {
                        updateLog(next);
                        return;
                    }
                    solving(next);
                }
        ));
    }

    private void updateLog(SearchState s) {
        int underestimate = s.getUnderestimate();
        if (underestimate < currentUpperBound) {
            currentUpperBound = underestimate;
            result = s;
        }
    }

    /**
     * Solves the scheduling problem with one processing core
     */
    private int topologicalSortSolve() {
        // copy edges, this method will alter the graph (a copy of it)
        Map<Vertex, fj.data.List<EdgeWithCost<Vertex>>> outwardEdges = new HashMap<>(graph.getOutwardEdgeMap());
        Map<Vertex, fj.data.List<EdgeWithCost<Vertex>>> inwardEdges = new HashMap<>(graph.getInwardEdgeMap());

        // list that will contain sorted vertices
        java.util.List<Vertex> sortedVertices = new ArrayList<>();
        int length = 0;
        // set of nodes with no incoming edge (dependency satisfied)
        Queue<Vertex> legalVertices = new LinkedList<>(graph.getVertices().parallelStream().filter(vertex -> inwardEdges.get(vertex).isEmpty()).collect(Collectors.toSet()));

        // exhaust vertices until all have been added to sorted list
        while(!legalVertices.isEmpty()){
            Vertex currVertex = legalVertices.remove();
            length += currVertex.getCost();
            // add vertex to tail
            sortedVertices.add(currVertex);

            // iterate and exhaust all edges: from currentVertex to vertexTo
            java.util.List<EdgeWithCost<Vertex>> edges = outwardEdges.get(currVertex).toJavaList();
            for (EdgeWithCost<Vertex> edge : new ArrayList<>(edges)){
                edges.remove(edge);
                // check vertexTo has no other inwardEdges (i.e. none excluding this one)
                Vertex vertexTo = edge.getTo();
                if (inwardEdges.get(vertexTo).toStream().filter(e -> !e.getFrom().equals(currVertex)).isEmpty()){
                    legalVertices.add(vertexTo);
                }
            }
        } // topological sort

        if (!sortedVertices.containsAll(graph.getVertices())){
            throw new IllegalStateException("TopologicalSort doesn't contain all vertices");
        }

        SearchState searchState = new SearchState();
        for (Vertex vertex : sortedVertices){
            searchState = new SearchState(searchState, vertex, 0);
        }
        result = searchState;
        return length;
    }

}
