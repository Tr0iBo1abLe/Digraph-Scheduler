package Solver;

import Graph.EdgeWithCost;
import Graph.Exceptions.GraphException;
import Graph.Graph;
import Graph.Vertex;
import Solver.Interfaces.ISolver;
import fj.data.Array;
import lombok.Data;

import java.util.*;

@Data
public class AStarSolver implements ISolver{
    private final Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private final int processorCount;

    @Override
    public void doSolve() {
        Queue<SearchState> queue = new PriorityQueue<>();
        Set<SearchState> set = new LinkedHashSet<>();
        queue.add(new SearchState(graph));

        while(true) {
            SearchState s = queue.remove();
            set.remove(s);
            if(s.getSize() == graph.getVertices().size()) {
                // We have found THE optimal solution
                scheduleVertices(s);
                return;
            }
            /* Expansion */
            for(Vertex v : s.getLegalVertices()) {
                for(int i = 0; i < processorCount; i++) {
                    SearchState next = new SearchState(s, v, i);
                    if(!set.contains(next)) {
                        set.add(next);
                        queue.add(next);
                    }
                }
            }
        }
    }

    private void scheduleVertices(SearchState s) {
        final int[] processors = Arrays.stream(s.getProcessors()).map(x -> x+1).toArray();
        final int[] startTimes = s.getStartTimes();
        for (int i = 0; i < graph.getVertices().size(); i++) {
            Vertex v = graph.lookUpVertexById(i);
            try {
                graph.scheduleVertex(v,processors[i], startTimes[i]);
            } catch (GraphException e) {
                e.printStackTrace();
            }
        }
    }
    /*
    OPEN ← emptyState
    while OPEN 6 = ∅ do s ← PopHead ( OPEN )
    if s is complete solution then return s as optimal solution
    Expand state s into children and compute f ( s child )
     for each OPEN ← new states
     */
}
