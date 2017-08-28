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
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Represents a partial schedule.
 * Include that current scheduled processor and their start time.
 * Includes A* cost function and pruning.
 *
 * @author Dovahkiin Huang, Will Molloy
 */
@Log4j
@Value
public class SearchState implements Comparable<SearchState>, ISearchState {
    @NonFinal
    private static Graph<Vertex, EdgeWithCost<Vertex>> graph;
    @NonFinal
    private static int processorCount;
    @Getter
    @NonFinal
    private static Vertex lastVertex = null;
    private final int[] processors;
    @Getter
    private final int[] startTimes;
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
        // clone() is slightly faster
        processors = prevState.processors.clone();
        startTimes = prevState.startTimes.clone();
        lastVertex = vertex;


        // The Scheduler Folding Function, responsible for figuring out the earliest possible
        // start time on the given processor ID
        F<Integer, F<Vertex, Integer>> schedulerFoldingFn = startTime -> v -> {
            // For each vertex on the SAME processor id, we find the last finish time,
            // which in turn, yields the earliest possible start time.
            int assignedId = v.getAssignedId();
            if (processors[assignedId] == processor) {
                int newTime = startTimes[assignedId] + graph.getVertex(assignedId).getCost();
                if (newTime > startTime) return newTime;
            }
            return startTime;
        };


        // The dependency folding function, responsible for finding out the minimal start time
        // given the parent is on a different processor.
        F<Integer, F<EdgeWithCost<Vertex>, Integer>> dependencyFoldingFn = startTime -> edge -> {
            // For each edge, we check if the parent task (vertex) has been scheduled and
            // not on the same processor, for this particular state.
            // If it has a parent (dependency) on a different processor, then the cost must include the
            // edge cost as well.
            // This function does not check if the actual dependencies are being satisfied,
            // users should use getLegalVertices() to ensure the Vertex can be scheduled.
            Vertex v = edge.getFrom();
            int assignedId = v.getAssignedId();
            if (processors[assignedId] != processor) {
                int newTime = this.startTimes[assignedId] + v.getCost() + edge.getCost();
                if (newTime > startTime) return newTime;
            }
            return startTime;
        };

        int startTime = 0;
        // Fold over the vertices and find the minimal cost given the same processor
        startTime = IterableW.wrap(graph.getVertices()).foldLeft(schedulerFoldingFn, startTime);
        // Fold over the parent vertices and find the minimal cost if there is a parent on another processor
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

    static void initialise(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        SearchState.graph = graph;
        SearchState.processorCount = processorCount;
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
        F<Boolean, F<Vertex, Boolean>> fn = bool -> vertex -> {
            if (bool.equals(true)) return bool;
            return processors[vertex.getAssignedId()] < 0;
        };
        graph.getVertices().forEach(vertex -> {
            if (processors[vertex.getAssignedId()] < 0) {
                // Skip any assigned processor
                if (graph.getParentVertices(vertex).foldLeft(fn, false)) return;
                // Add the available vertex to the set
                set.add(vertex);
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
     * equals() will be called whenever queue.contains() is called and will compare the newly created state to ALL other
     * states in the queue ***this method is called tens of millions of times***.
     * Therefore this method needs to be very efficient and A* solve time heavily depends on it.
     * <p>
     * To minimise A* solve time it's best to have as few initial states as possible as each grows into a large subtree.
     * Mirrored/shuffled schedules will be ignored, i.e. once a task is placed on a core it will only be placed on
     * another core if it's startTime is different.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        SearchState rhs = (SearchState) obj;

        EqualsBuilder builder = new EqualsBuilder();
        return builder.append(startTimes, rhs.startTimes).isEquals() && processorsAreShuffled(rhs.processors);
    }

    /**
     * Determines if processors are shuffled for this SearchState vs. some other SearchState. This is done by checking
     * if duplicates in the array map to the same value. Therefore detecting if the SearchState is a mirror of the other.
     * For example:
     * this.processors = [ 1, 1, 2, 2, 3, 3 ]. other.processors = [ 5, 5, 8, 8, 6, 6 ]. Returns true, all duplicates map to the same value.
     * this.processors = [ 7, 7, 7, 4, 4, 5 ]. other.processors = [ 8, 8, 8, 5, 6, 7 ]. Returns false, one 4 maps to 5, the other to 6.
     * this.processors = [ 7, 7, 7, 4, 4, 5 ]. other.processors = [ 8, 8, 8, 5, 5, 8 ]. Returns false, both 7 and 5 map to 8.
     */
    private boolean processorsAreShuffled(final int[] otherStateProcessors) {
        int[] processorMap = new int[processorCount]; // to cache the mapped values
        Arrays.fill(processorMap, -1);

        for (int i = 0; i < processors.length; i++) {
            if (processors[i] < 0) continue; // check vertex has been assigned a processor
            if (processorMap[processors[i]] < 0) { // check if value is mapped for this processor
                if (contains(processorMap, otherStateProcessors[i])) return false; // check for duplicate mapping
                processorMap[processors[i]] = otherStateProcessors[i]; // if not already mapped, initialise
            } else if (processorMap[processors[i]] != otherStateProcessors[i]) { // check mapping is correct
                return false;
            }
        }
        return true;
    }

    private boolean contains(final int[] array, final int value) {
        for (int i : array) {
            if (i == value) return true;
        }
        return false;
    }

    /**
     * Match equals(), only considers equal if equals() matches hashcode(), very important!
     * <p>
     * Large primes, these values can SIGNIFICANTLY change solve time since they affect the number of hash collisions
     * Credit for primes table: Aaron Krowne
     * http://br.endernet.org/~akrowne/
     * http://planetmath.org/encyclopedia/GoodHashTablePrimes.html
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(805306457, 1610612741) // primes
                .append(startTimes)
                .append(processorsAreShuffled(processors))
                .toHashCode();
    }
}
