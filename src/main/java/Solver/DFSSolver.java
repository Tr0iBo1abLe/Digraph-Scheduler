package Solver;

import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import javafx.application.Platform;
import lombok.extern.log4j.Log4j;

import java.util.Timer;
import java.util.TimerTask;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * DFS Solver, uses branch and bound technique to prune search states in the stack.
 * Doesn't use much memory since the current best state is cached while the stack is cleared on each iteration.
 * <p>
 * Created by mason on 31/07/17.
 *
 * @author Mason Shi, Dovahkiin Huang, Will Molloy
 */
@Log4j
public final class DFSSolver extends AbstractSolver {

    private int currUpperBound;

    private SearchState intermediateState; // represent a partial schedule which is going to be used by GUI updater until Solver finishes.

    public DFSSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
        log.debug("Solver inited");
    }

    DFSSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount, SearchState existingState) {
        super(graph, processorCount, existingState);
        log.debug("Solver inited with an existing state");
        currBestState = existingState;
    }

    /**
     * Used when transferring state from a AStarSolver
     */
    void completeSolve() {
        // The upper bound is now the currBestState + that of scheduling the remaining vertices to the same processor.
        // If there is an edge pointing to these vertices we can assume the 'same' processor is the optimal one
        currUpperBound = currBestState.getUnderestimate() + currBestState.getUnAssignedVertices().stream().mapToInt(vertex -> vertex.getCost()).sum();
        setupGuiTimer(); //ensure a gui timer if required
        solving(currBestState);
        if (updater != null && timer != null) {
            Platform.runLater(() -> updater.update(currBestState, this)); // required by FX framework
            timer.cancel();
        }
    }

    /**
     * This method ensures a gui timer (see #Timer timer in #AbstractSolver) gets initialized if there exists GUI components.
     * No matter the DFS solver starts up on it own or is called by AS to continue on existing tasks
     */
    private void setupGuiTimer(){
        if (updater != null) {
            /* We have an updater and a UI to update */
            isUpdatableProgressBar = true;
            AbstractSolver solver = this; //provide a reference to GUI classes
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                                          @Override
                                          public void run() { Platform.runLater(() -> {updater.update(intermediateState, solver);});} // required by FX framework
                                      },
                    100, 100);
        }
    }

    @Override
    public void doSolve() {
        setupGuiTimer(); //ensure a gui timer if required
        // Upper bound is initially topological sort i.e. all the nodes scheduled to one processor (when edge cost can be ignored)
        doTopologicalSortSolveAndSetInitialUpperBound();
        if (processorCount > 1) { // when processorCount = 1, topologicalSort is the solution.
            SearchState searchState = new SearchState();
            solving(searchState);
        }
        if (updater != null && timer != null) {
            Platform.runLater(() -> updater.update(currBestState, this)); // required by FX framework
            timer.cancel();
        }
    }

    private void solving(SearchState currState) {
        currState.getLegalVertices().forEach(vertex -> IntStream.range(0, processorCount).forEach(processor -> {
                    SearchState nextState = new SearchState(currState, vertex, processor);
                    intermediateState = nextState;
                    if (nextState.getUnderestimate() >= currUpperBound) {
                        return;
                    }
                    if (nextState.getNumVertices() == graph.getVertices().size()) {
                        updateLog(nextState);
                        return;
                    }
                    //increase the state counter for GUI, process only when there is a GUI to update
                    if (isUpdatableProgressBar){ //true if there is a GUI progress bar needs to be updated
                        stateCounter++;
                    }
                    solving(nextState);
                }
        ));
    }

    private void updateLog(SearchState s) {
        int underestimate = s.getUnderestimate();
        if (underestimate < currUpperBound) {
            currUpperBound = underestimate;
            currBestState = s;
        }
    }

    /**
     * Solves the scheduling problem with one processing core
     */
    private void doTopologicalSortSolveAndSetInitialUpperBound() {
        // parallelStream() is slower due to overhead and these graphs are expected to be <20 nodes.
        // copy edges, this method will alter the graph (a copy of it).

        // Convert FJ List to a java list for mutability
        Map<Vertex, java.util.List<EdgeWithCost<Vertex>>> outwardEdges = new HashMap<>();
        graph.getOutwardEdgeMap().forEach((vertex, edgeWithCostsList) -> outwardEdges.put(vertex, edgeWithCostsList.toJavaList()));
        Map<Vertex, java.util.List<EdgeWithCost<Vertex>>> inwardEdges = new HashMap<>();
        graph.getInwardEdgeMap().forEach((vertex, edgeWithCostsList) -> inwardEdges.put(vertex, edgeWithCostsList.toJavaList()));

        // list that will contain sorted vertices
        java.util.List<Vertex> sortedVertices = new ArrayList<>();
        // set of nodes with no incoming edge (dependency satisfied)
        Queue<Vertex> legalVertices = new LinkedList<>(graph.getVertices().stream().filter(vertex -> inwardEdges.get(vertex).isEmpty()).collect(Collectors.toSet()));

        // exhaust vertices until all have been added to sorted list`
        while (!legalVertices.isEmpty()) {
            Vertex currVertex = legalVertices.remove();

            // add vertex to tail of sorted list
            sortedVertices.add(currVertex);

            // Iterate and exhaust all edges, from: currentVertex to: vertexTo
            Queue<EdgeWithCost<Vertex>> edgesFromCurrVertex = new LinkedList<>(outwardEdges.get(currVertex));
            while (!edgesFromCurrVertex.isEmpty()) {
                EdgeWithCost edge = edgesFromCurrVertex.remove();
                Vertex vertexTo = edge.getTo();

                // Remove the edge from the graph (both from outward and inward edge maps)
                outwardEdges.put(currVertex, new ArrayList<>(edgesFromCurrVertex));
                // This is re adding all edges excluding the current one i.e. updating the value for vertexTo
                inwardEdges.put(vertexTo, inwardEdges.get(vertexTo).stream().filter(e -> !e.equals(edge)).collect(Collectors.toList()));

                // check vertexTo has no other inwardEdges (i.e. none excluding this one)
                if (inwardEdges.get(vertexTo).isEmpty()) {
                    legalVertices.add(vertexTo);
                }
            }
        }

        // Schedule the vertices all on the first core.
        final SearchState[] searchState = {new SearchState()};
        sortedVertices.forEach(vertex -> searchState[0] = new SearchState(searchState[0], vertex, 0));

        // Set initial upper bound and currBestState state
        currUpperBound = searchState[0].getUnderestimate();
        currBestState = searchState[0];
    }
}
