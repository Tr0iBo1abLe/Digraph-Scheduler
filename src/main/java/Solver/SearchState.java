package Solver;

import CommonInterface.ISearchState;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import fj.F;
import fj.data.IterableW;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
public class SearchState implements Comparable<SearchState>, ISearchState {
    @NonFinal
    private static Graph<Vertex, EdgeWithCost<Vertex>> graph;
    @NonFinal
    private static int processorCount;
    private final int[] processors;
    @Getter
    private final int[] startTimes;
    private Vertex lastVertex;
    @Getter
    private int numVertices;
    @NonFinal
    private int underestimate;

    static void initialise(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        SearchState.graph = graph;
        SearchState.processorCount = processorCount;
    }

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

        int startTime = 0;
        /* Fold over the vertices and find the minimal cost given the same processor */
        startTime = IterableW.wrap(graph.getVertices()).foldLeft(schedulerFoldingFn, startTime);
        /* Fold over the parent vertices and find the minimal cost if there is a parent on another processor */
        startTime = graph.getInwardsEdges(vertex).foldLeft(dependencyFoldingFn, startTime);

        // Store the result we obtained
        processors[vertex.getAssignedId()] = processor;
        startTimes[vertex.getAssignedId()] = startTime;

        // Underestimate function
        int nextPriority = staticCostFunction(startTime, vertex);

        if (underestimate < nextPriority)
            underestimate = nextPriority;

        numVertices = prevState.getNumVertices() + 1;
    }

    /**
     * Initial cost set for a state; used as a static priority for picking the next state in the search.
     */
    private int staticCostFunction(int startTime, Vertex vertex) {
        return startTime + vertex.getCost() + vertex.getBottomLevel();
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

    /**
     * A* pruning is done here:
     *
     * Equals is different depending on the input.
     * The idea is to ignore mirrored states however this can be done differently:
     *
     * Ignoring "processors" causes only the initial states to not be mirrored and its effect is better with a larger
     * core count, while ignoring "startTimes" has an initial greater number of states it will ignore similar states
     * later on (for a smaller core count).
     *
     * I've come to the conclusion that (generally) with >2 processors for scheduling ignore "processors" is better.
     * Ignoring startTimes yields (approximately): numInitialLegalVertices * processorCount initial states.
     * While ignore processors yields: numInitialLegalVertices initial states.
     * While ignoring both yields: numInitialLegalVertices / processorCount initial states.
     * However it also depends on number of edges and other things.
     *
     * Ignoring both is wrong later into the search because vertices that can be scheduled with startTime 0 can no longer
     * be due to other nodes taking up those places.
     * Therefore ignoring both can only be done during the initial iteration, where all vertices that CAN have a startTime of 0
     * have these states created and are put into the queue.
     *
     * A possible problem is many vertices that have no edges (many initial legal vertices) + few vertices with edges
     * (few dependent vertices) where numInitialLegalVertices > processorCount so not all are considered with startTime of 0
     * leading to the final schedule to maybe not be optimal. TODO So i'm still experimenting/testing.
     */
    @Override
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        SearchState rhs = (SearchState) obj;
        EqualsBuilder builder =  new EqualsBuilder()
                .append(lastVertex, rhs.lastVertex)
                .append(numVertices, rhs.numVertices)
                .append(underestimate, rhs.underestimate);

        // cut initial size down; leading to a much smaller tree, this significantly changes solve time but can be unstable
        // we can ignore both "processors" and "startTimes" at the beginning;
        // TODO ??need to consider legalVertices not just numVertices ??
        if (numVertices <= processorCount){
            return builder.isEquals();
        }
        if (processorCount > 2){
            return builder.append(startTimes, rhs.startTimes).isEquals(); // Ignoring "processors"
        }
        return builder.append(processors, rhs.processors).isEquals(); // Ignoring "startTimes"
    }

    /**
     * Match equals() to improve hash table lookup.
     */
    @Override
    public int hashCode(){
        HashCodeBuilder builder =  new HashCodeBuilder(37, 59) // primes
                .append(lastVertex)
                .append(numVertices)
                .append(underestimate);
        if (numVertices <= processorCount){
            return builder.toHashCode();
        }
        if (processorCount > 2){
            return builder.append(startTimes).toHashCode();
        }
        return builder.append(processors).toHashCode();
    }

}
