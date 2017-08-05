package Solver;

import fj.F;
import fj.data.IterableW;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A class of partial solution
 */
public class SearchState implements Comparable<SearchState>{
    @Getter
    private static Graph graph;
    @Getter
    private static int totalSize;
    @Getter
    private int priority;
    @Getter
    private Node lastVertex;
    @Getter
    private final int[] processors;
    @Getter
    private final double[] startTimes;
    @Getter
    private int size;

    @Getter
    private int DFcost;

    public static void init(Graph g) {
        graph = g;
        totalSize = g.getNodeCount();
    }

    public SearchState() {
        this.priority = 0;
        this.size = 0;
        this.lastVertex = null;
        this.processors = Arrays.stream(new int[totalSize]).map(_i -> -1).toArray();
        this.startTimes = Arrays.stream(new double[totalSize]).map(_i -> -1).toArray();
    }

    public SearchState(SearchState prevState, Node vertex, int processorId) {
        this.priority = prevState.priority;
        this.size = prevState.size;
        this.processors = Arrays.copyOf(prevState.processors, prevState.processors.length);
        this.startTimes = Arrays.copyOf(prevState.startTimes, prevState.startTimes.length);
        this.lastVertex = vertex;

        this.size++;

        F<Double, F<Edge, Double>> dependencyFoldingFn = t -> e -> {
            int aid = e.getSourceNode().getIndex();
            if(this.processors[aid] != processorId && this.processors[aid] != -1) {
                double newTime = (this.startTimes[aid] + (Double)e.getSourceNode().getAttribute("Weight") + (Double)e.getAttribute("Weight"));
                if(newTime > t) return newTime;
            }
            return t;
        };

        F<Double, F<Node, Double>> schedulerFoldingFn = t -> v -> {
            int id = v.getIndex();
            if(this.processors[id] == processorId && this.processors[id] != -1) {
                Double newTime = (this.startTimes[id] + (Double)graph.getNode(id).getAttribute("Weight"));
                if(newTime > t) return newTime;
            }
            return t;
        };

        double time = 0;
        final IterableW<Node> iterableV = IterableW.wrap(graph.getNodeSet());
        final IterableW<Edge> iterableP = IterableW.wrap(lastVertex.getEachEnteringEdge());
        time = iterableV.foldLeft(schedulerFoldingFn, time);
        time = iterableP.foldLeft(dependencyFoldingFn, time);

        this.processors[this.lastVertex.getIndex()] = processorId;
        this.startTimes[this.lastVertex.getIndex()] = time;

        int nextP = (int) (time + ((Double) this.lastVertex.getAttribute("Weight")) + ((Double) this.lastVertex.getAttribute("BL")));

        if(this.priority < nextP) {
            this.priority = nextP;
        }
        
        DFcost = (int) (time + ((Double) lastVertex.getAttribute("Weight")));
        
    }

    Set<Node> getLegalVertices() {
        Set<Node> set = new HashSet<>();
        F<Boolean, F<Object, Boolean>> fn = b -> v -> {
            if(b.equals(true)) return b;
            Node n = (Node) v;
            return processors[n.getIndex()] < 0;
        };
        next:
        for(int i = 0; i < totalSize; i++) {
            Node v = graph.getNode(i);
            if(processors[i] < 0) {
                final IterableW<Object> wrap = IterableW.wrap(v.getEnteringEdgeSet().stream().map(Edge::getSourceNode).collect(Collectors.toSet()));
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

}
