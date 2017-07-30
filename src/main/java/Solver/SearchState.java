package Solver;

import Graph.Graph;
import Graph.Vertex;
import Graph.EdgeWithCost;
import fj.F;
import fj.F2;
import fj.F2Functions;
import fj.data.Array;
import fj.data.IterableW;
import fj.data.List;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A class of partial solution
 */
public class SearchState implements Comparable<SearchState>{
    private final Graph<Vertex, EdgeWithCost<Vertex>> graph;
    @Getter
    private int size;
    @Getter
    private int priority;
    @Getter
    private Vertex lastVertex;
    @Getter
    private final int[] processors;
    @Getter
    private final int[] startTimes;
    @Getter
    private final int graphSize;

    static {

    }

    public SearchState(Graph<Vertex,EdgeWithCost<Vertex>> graph) {
        this.graph = graph;
        this.size = 0;
        this.priority = 0;
        this.lastVertex = null;
        this.graphSize = this.graph.getVertices().size();
        this.processors = Arrays.stream(new int[graphSize]).map(_i -> -1).toArray();
        this.startTimes = new int[graphSize];
    }

    public SearchState(SearchState prevState, Vertex vertex, int processorId) {
        this.graph = prevState.graph;
        this.size = prevState.size;
        this.priority = prevState.priority;
        this.graphSize = prevState.graphSize;
        this.processors = Arrays.copyOf(prevState.processors, prevState.processors.length);
        this.startTimes = Arrays.copyOf(prevState.startTimes, prevState.startTimes.length);
        this.lastVertex = vertex;

        this.size++;

        F<Integer, F<Vertex, Integer>> dependencyFoldingFn = t -> v -> {
            int aid = v.getAssignedId();
            if(isProcessorValid(aid, processorId)) {
                int newTime = startTimes[aid] + v.getCost() + graph.getForwardEdge(v, lastVertex).getCost();
                if(newTime > t) return newTime;
            }
            return t;
        };

        F<Integer, F<Vertex, Integer>> schedulerFoldingFn = t -> v -> {
            int id = v.getAssignedId();
            if(isProcessorValid(id, processorId)) {
                int newTime = startTimes[id] + v.getCost();
                if(newTime > t) return newTime;
            }
            return t;
        };

        int time = 0;

        final IterableW<Vertex> iterableV = IterableW.wrap(graph.getReverseVertices(lastVertex));
        final IterableW<Vertex> iterableP = IterableW.wrap(graph.getReverseVertices(lastVertex));
        time = iterableV.foldLeft(schedulerFoldingFn, time);
        time = iterableP.foldLeft(dependencyFoldingFn, time);

        processors[lastVertex.getAssignedId()] = processorId;
        startTimes[lastVertex.getAssignedId()] = time;

        int nextP = time + lastVertex.getCost() + lastVertex.getBottomLevel();

        if(this.priority < nextP) {
            this.priority = nextP;
        }
    }

    private boolean isProcessorValid(final int n, final int id) {
        return processors[n] == id && processors[n] != -1;
    }

    private Integer get(Array<Integer> a, final int n) {
        return a.get(n);
    }

    Set<Vertex> getLegalVertices() {
        Set<Vertex> set = new HashSet<>();
        F<Boolean, F<Vertex, Boolean>> fn = b -> v -> {
            if(b.equals(true)) return b;
            return processors[v.getAssignedId()] < 0;
        };
        next:
        for(int i = 0; i < this.graphSize; i++) {
            Vertex v = graph.lookUpVertexById(i);
            if(processors[i] < 0) {
                final IterableW<Vertex> wrap = IterableW.wrap(graph.getReverseVertices(v));
                if(wrap.foldLeft(fn, false)) continue next;
                set.add(v);
            }
        }
        return set;
    }

    @Override
    public int compareTo(SearchState searchState) {
        return this.priority - searchState.priority;
    }
}
