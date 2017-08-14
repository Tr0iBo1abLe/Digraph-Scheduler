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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * A class of partial solution
 */
@EqualsAndHashCode
public class SearchState implements Comparable<SearchState>, ISearchState {
    @Getter
    private static Graph<Vertex, EdgeWithCost<Vertex>> graph;
    @Getter
    private static int totalSize;
    @Getter
    private final int[] processors;
    @Getter
    private final int[] startTimes;
    @Getter
    private int priority;
    @Getter
    private Vertex lastVertex;
    @Getter
    private int size;

    public static void initialise(Graph<Vertex, EdgeWithCost<Vertex>> graph) {
        SearchState.graph = graph;
        totalSize = graph.getVertices().size();
    }

    public SearchState() {
        this.priority = 0;
        this.size = 0;
        this.lastVertex = null;
        this.processors = Arrays.stream(new int[totalSize]).map(_i -> -1).toArray();
        this.startTimes = Arrays.stream(new int[totalSize]).map(_i -> -1).toArray();
    }

    public SearchState(SearchState prevState, Vertex vertex, int processorId) {
        this.priority = prevState.priority;
        this.size = prevState.size;
        this.processors = Arrays.copyOf(prevState.processors, prevState.processors.length);
        this.startTimes = Arrays.copyOf(prevState.startTimes, prevState.startTimes.length);
        this.lastVertex = vertex;

        this.size++;

        F<Integer, F<EdgeWithCost<Vertex>, Integer>> dependencyFoldingFn = t -> e -> {
            Vertex v = e.getFrom();
            int aid = v.getAssignedId();
            if (this.processors[aid] != processorId && this.processors[aid] != -1) {
                int newTime = this.startTimes[aid] + v.getCost() + e.getCost();
                if (newTime > t) return newTime;
            }
            return t;
        };

        F<Integer, F<Vertex, Integer>> schedulerFoldingFn = t -> v -> {
            int id = v.getAssignedId();
            if (this.processors[id] == processorId && this.processors[id] != -1) {
                int newTime = this.startTimes[id] + graph.getVertex(id).getCost();
                if (newTime > t) return newTime;
            }
            return t;
        };

        int time = 0;
        final IterableW<Vertex> iterableV = IterableW.wrap(graph.getVertices());
        time = iterableV.foldLeft(schedulerFoldingFn, time);
        time = graph.getInwardsEdges(lastVertex).foldLeft(dependencyFoldingFn, time);

        this.processors[this.lastVertex.getAssignedId()] = processorId;
        this.startTimes[this.lastVertex.getAssignedId()] = time;

        int nextPriority = time + this.lastVertex.getCost() + this.lastVertex.getBottomLevel();

        if (this.priority < nextPriority) {
            this.priority = nextPriority;
        }


    }


    Set<Vertex> getLegalVertices() {
        Set<Vertex> set = new HashSet<>();
        F<Boolean, F<Vertex, Boolean>> fn = b -> v -> {
            if (b.equals(true)) return b;
            return processors[v.getAssignedId()] < 0;
        };
        IntStream.range(0, totalSize).forEach(i -> {
            Vertex v = graph.getVertex(i);
            if (processors[i] < 0) {
                // Skip any assigned processor
                if (graph.getParentVertices(v).foldLeft(fn, false)) return;
                // Add the available vertex to the set
                set.add(v);
            }
        });
        return set;
    }

    @Override
    public int compareTo(@NonNull SearchState searchState) {
        return this.priority - searchState.priority;
    }

}
