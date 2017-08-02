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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

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
    @Getter
    private static Graph<Vertex, EdgeWithCost<Vertex>> graph;
    @Getter
    private static int totalSize;
    @Getter
    private int priority;
    @Getter
    private Vertex lastVertex;
    @Getter
    private final int[] processors;
    @Getter
    private final int[] startTimes;
    @Getter
    private int size;

    @Getter
    private int DFcost;

    public static void init(Graph<Vertex, EdgeWithCost<Vertex>> g) {
        graph = g;
        totalSize = g.getVertices().size();
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

        F<Integer, F<Vertex, Integer>> dependencyFoldingFn = t -> v -> {
            int aid = v.getAssignedId();
            if(this.processors[aid] != processorId && this.processors[aid] != -1) {
                int newTime = this.startTimes[aid] + v.getCost() + graph.getForwardEdge(v, this.lastVertex).getCost();
                if(newTime > t) return newTime;
            }
            return t;
        };

        F<Integer, F<Vertex, Integer>> schedulerFoldingFn = t -> v -> {
            int id = v.getAssignedId();
            if(this.processors[id] == processorId && this.processors[id] != -1) {
                int newTime = this.startTimes[id] + graph.lookUpVertexById(id).getCost();
                if(newTime > t) return newTime;
            }
            return t;
        };

        int time = 0;
        final IterableW<Vertex> iterableV = IterableW.wrap(graph.getVertices());
        final IterableW<Vertex> iterableP = IterableW.wrap(graph.getReverseVertices(lastVertex));
        time = iterableV.foldLeft(schedulerFoldingFn, time);
        time = iterableP.foldLeft(dependencyFoldingFn, time);

        this.processors[this.lastVertex.getAssignedId()] = processorId;
        this.startTimes[this.lastVertex.getAssignedId()] = time;

        int nextP = time + this.lastVertex.getCost() + this.lastVertex.getBottomLevel();

        if(this.priority < nextP) {
            this.priority = nextP;
        }
        
        DFcost = time + lastVertex.getCost();
        
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
        for(int i = 0; i < totalSize; i++) {
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
    public int compareTo(@NonNull SearchState searchState) {
        return this.priority - searchState.priority;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
