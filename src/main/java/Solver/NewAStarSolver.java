package Solver;

import Solver.Interfaces.ISolver;
import lombok.Data;
import org.graphstream.graph.Graph;

import java.util.*;
import java.util.stream.IntStream;

@Data
public class NewAStarSolver implements ISolver {

    private final Graph graph;
    private final int procN;

    @Override
    public void doSolve() {
        NewSearchState.init(graph);
        System.out.println(graph.getNodeCount());

        Queue<NewSearchState> queue = new PriorityQueue<>();
        Set<NewSearchState> set = new HashSet<>();
        queue.add(new NewSearchState());

        while(true) {
            NewSearchState s = queue.remove();
            set.remove(s);
            if(s.getSize() == graph.getNodeCount()) {
                // We have found THE optimal solution
                scheduleVertices(s);
                return;
            }
            s.getLegalVertices().forEach( v -> {
                IntStream.of(0, procN-1).forEach(i -> {
                            NewSearchState next = new NewSearchState(s, v, i);
                            if(!set.contains(next)) {
                                set.add(next);
                                queue.add(next);
                            }
                        }
                );
            });
        }

    }

    private void scheduleVertices(NewSearchState s) {

        final int[] processors = Arrays.stream(s.getProcessors()).map(x -> x+1).toArray();
        final double[] startTimes = s.getStartTimes();
        graph.getNodeSet().forEach(v -> {
            int id = v.getIndex();
            v.addAttribute("Processor", processors[id]);
            v.addAttribute("ST", startTimes[id]);
        });
    }
}
