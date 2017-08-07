package Solver;

import CommonInterface.ISolver;
import lombok.Data;
import lombok.Getter;
import org.graphstream.graph.Graph;

import java.util.Arrays;

@Data
abstract public class AbstractSolver implements ISolver {
    @Getter
    protected final Graph graph;
    protected final int processorCount;

    void scheduleVertices(SearchState s) {
        final int[] processors = Arrays.stream(s.getProcessors()).map(x -> x+1).toArray();
        final int[] startTimes = s.getStartTimes();
        graph.getNodeSet().forEach(v -> {
            int id = v.getIndex();
            v.addAttribute("Processor", processors[id]);
            v.addAttribute("ST", startTimes[id]);
        });
    }
}
