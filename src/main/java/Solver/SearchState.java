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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
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
    private final int[] processors;
    @Getter
    private final int[] startTimes;
    @Getter
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
     * Get the total cost of Vertices that haven't got an assigned processor (or startTime)
     * i.e. cost if they were all assigned to the same processor (topologically sorted).
     *
     * @return the total cost of the set of un assigned vertices.
     */
    int getTotalCostOfUnassignedVertices() {
        return getUnassignedVertices().stream().mapToInt(vertex -> vertex.getCost()).sum();
    }

    /**
     * Get the set of unassigned vertices.
     *
     * @return the set of un assigned vertices.
     */
    private Set<Vertex> getUnassignedVertices() {
        return graph.getVertices().stream().filter(vertex -> processors[vertex.getAssignedId()] < 0).collect(Collectors.toSet());
    }

    /**
     * Get the set of unassigned vertices.
     *
     * @return the set of assigned vertices.
     */
    private Set<Vertex> getAssingedVertices() {
        return graph.getVertices().stream().filter(vertex -> processors[vertex.getAssignedId()] > -1).collect(Collectors.toSet());
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
     * Therefore more aggressive pruning is done at the beginning of the search:
     * Both startTimes and processorCount will be ignored initially, meaning once a task is placed on a core its other
     * positions won't be considered. This is only done for the initial set of legal vertices with the condition the size
     * of this set is less than or equal to the processorCount to ensure they are all considered with a startTime of 0.
     * <p>
     * After this initial set of vertices mirrored/shuffled schedules will be ignored, i.e. once a task is placed on a
     * core it will only be placed on another core if it's startTime is different.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        SearchState rhs = (SearchState) obj;
        EqualsBuilder builder = new EqualsBuilder()
                .append(lastVertex, rhs.lastVertex)
                .append(numVertices, rhs.numVertices)
                .append(underestimate, rhs.underestimate);

        // Note: getLegalVertices() is expensive, so it's short circuited. Two ANDs saves 500ms (20%) for 11node 2core input
        if (numVertices <= processorCount && getLegalVertices().size() + numVertices <= processorCount) {
            // Cut initial size down; leading to a much smaller tree significantly reducing solve time
            // We can ignore both "processors" and "startTimes" at the beginning: provided all the legal vertices can
            // be scheduled with a startTime of 0.
            log.debug("equals(): Ignore both");
            return builder.isEquals();
        }
        if (processorCount > 2) {
            // Ignoring "processors" (assigned processor), ignores shuffled (i.e. mirrored) schedules
            // i.e. those where vertices have the same startTimes on different cores.
            return builder.append(startTimes, rhs.startTimes).isEquals();
        }
        // Ignoring "startTimes", only correct for 2 (or 1) cores. Effectively ignores mirrors but will be greedy later
        // in the search ignoring schedules where tasks are placed with a later startTime.
        // Incorrect for >2 cores because of the case where tasks have multiple inward edges meaning they may may be
        // placed with a later startTime first and then the earlier startTime will be ignored.
        return builder.append(processors, rhs.processors).isEquals();
    }

    /**
     * Match equals() to improve hash table lookup.
     */
    @Override
    public int hashCode() {
        // primes, these values can SIGNIFICANTLY change solve time since they affect the number of hash collisions
        // Credit for primes table: Aaron Krowne
        // http://br.endernet.org/~akrowne/
        // http://planetmath.org/encyclopedia/GoodHashTablePrimes.html
        HashCodeBuilder builder = new HashCodeBuilder(805306457, 1610612741) // 2147483647, 1610612741, 805306457
                .append(lastVertex)
                .append(numVertices)
                .append(underestimate);
        if (numVertices <= processorCount && getLegalVertices().size() + numVertices <= processorCount) {
            return builder.toHashCode();
        }
        if (processorCount > 2) {
            return builder.append(startTimes).toHashCode();
        }
        return builder.append(processors).toHashCode();
    }

}
