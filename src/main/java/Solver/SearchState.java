package Solver;

import CommonInterface.ISearchState;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import fj.F;
import fj.data.IterableW;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents a partial schedule.
 * Include that current scheduled processor and their start time.
 *
 * @author Dovahkiin Huang, Will Molloy
 */

@Value
@EqualsAndHashCode(exclude = {"processors",""})
// exclude partial schedules where nodes only differ by their processor TODO this needs testing!
public class SearchState implements Comparable<SearchState>, ISearchState {
    @NonFinal
    private static Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private final int[] processors;
    @Getter
    private final int[] startTimes;
    private Vertex lastVertex;
    @Getter
    private int numVertices;
    @NonFinal
    private int underestimate;

    SearchState() {
        underestimate = 0;
        numVertices = 0;
        lastVertex = null;
        int graphVerticesSize = graph.getVertices().size();
        processors = IntStream.generate(() -> -1).limit(graphVerticesSize).toArray();
        startTimes = processors.clone();
    }

    SearchState(SearchState prevState, Vertex vertex, int processor) {
        underestimate = prevState.underestimate;
        /* clone() is slightly faster */
        processors = prevState.processors.clone();
        startTimes = prevState.startTimes.clone();
        lastVertex = vertex;

        /*
         * The Scheduler Folding Function, responsible for figuring out the earliest possible
         * start time on the given processor ID
         */
        F<Integer, F<Vertex, Integer>> schedulerFoldingFn = t -> v -> {
            // For each vertex on the SAME processor id, we find the last finish time,
            // which in turn, yields the earliest possible start time.
            int aid = v.getAssignedId();
            if (processors[aid] == processor) {
                int newTime = startTimes[aid] + graph.getVertex(aid).getCost();
                if (newTime > t) return newTime;
            }
            return t;
        };

        /*
         * The dependency folding function, responsible for finding out the minimal start time
         * given the parent is on a different processor.
         */
        F<Integer, F<EdgeWithCost<Vertex>, Integer>> dependencyFoldingFn = t -> e -> {
            // For each edge, we check if the parent task (vertex) has been scheduled and
            // not on the same processor, for this particular state.
            // If it has a parent (dependency) on a different processor, then the cost must include the
            // edge cost as well.
            // This function does not check if the actual dependencies are being satisfied,
            // users should use getLegalVertices() to ensure the Vertex can be scheduled.
            Vertex v = e.getFrom();
            int aid = v.getAssignedId();
            if (processors[aid] != processor) {
                int newTime = this.startTimes[aid] + v.getCost() + e.getCost();
                if (newTime > t) return newTime;
            }
            return t;
        };


        int time = 0;
        /*
         * Alternative scheduler implementation, performs marginally worse than functional
         * java variant. For demo purpose only(?)
        */
        /*
        time = IntStream.range(0, processors.length).reduce(0, (acc, n) -> {
            if (processors[n] == processorId) {
                int newTime = startTimes[n]  + graph.getVertex(n).getCost();
                if (newTime > acc) return newTime;
            }
            return acc;
        });
        */
        /* Fold over the vertices and find the minimal cost given the same processor */
        time = IterableW.wrap(graph.getVertices()).foldLeft(schedulerFoldingFn, time);
        /* Fold over the parent vertices and find the minimal cost if there is a parent on another processor */
        time = graph.getInwardsEdges(vertex).foldLeft(dependencyFoldingFn, time);

        // Store the result we obtained
        processors[vertex.getAssignedId()] = processor;
        startTimes[vertex.getAssignedId()] = time;

        // Underestimate function
        int nextPriority = time + vertex.getCost() + vertex.getBottomLevel();

        if (underestimate < nextPriority)
            underestimate = nextPriority;

        numVertices = prevState.getNumVertices() + 1;
    }

    static void initialise(Graph<Vertex, EdgeWithCost<Vertex>> graph) {
        SearchState.graph = graph;
    }

    /**
     * Get the legal vertices of a state, meaning the dependencies have been satisfied for these vertices.
     *
     * @return the set of legal vertices
     */
    Set<Vertex> getLegalVertices() {
        Set<Vertex> set = new HashSet<>();
        F<Boolean, F<Vertex, Boolean>> fn = b -> v -> {
            if (b.equals(true)) return b;
            return processors[v.getAssignedId()] < 0;
        };
        graph.getVertices().forEach(v -> {
            if (processors[v.getAssignedId()] < 0) {
                // Skip any assigned processor
                if (graph.getParentVertices(v).foldLeft(fn, false)) return;
                // Add the available vertex to the set
                set.add(v);
            }
        });
        return set;
    }

    /**
     * Get the set of Vertices that haven't got an assigned processor (or startTime)
     *
     * @return the set of un assigned vertices.
     */
    Set<Vertex> getUnAssignedVertices() {
        return graph.getVertices().stream().filter(vertex -> processors[vertex.getAssignedId()] < 0).collect(Collectors.toSet());
    }


    /**
     * Needed for Comparable interface
     *
     * @param searchState
     * @return diff in priority
     * @see java.util.PriorityQueue
     * @see Comparable
     */
    @Override
    public int compareTo(@NonNull SearchState searchState) {
        return this.underestimate - searchState.underestimate;
    }

}
