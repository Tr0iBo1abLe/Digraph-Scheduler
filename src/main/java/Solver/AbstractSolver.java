package Solver;

import CommonInterface.ISearchState;
import CommonInterface.ISolver;
import GUI.IUpdatableState;
import lombok.Data;
import lombok.Getter;
import org.graphstream.graph.Graph;

import java.util.Arrays;

import static oracle.jrockit.jfr.events.Bits.intValue;

@Data
abstract public class AbstractSolver implements ISolver {
    @Getter
    protected final Graph graph;
    @Getter
    protected final int processorCount;
    protected GUIUpdater updater;
    @Getter
    protected int finalTime;

    void scheduleVertices(SearchState s) {
        final int[] processors = Arrays.stream(s.getProcessors()).map(x -> x+1).toArray();
        final int[] startTimes = s.getStartTimes();
        final int[] max = {-1};
        graph.getNodeSet().forEach(v -> {
            int id = v.getIndex();
            v.addAttribute("Processor", processors[id]);
            v.addAttribute("Start", startTimes[id]);

            // get the ending time
            int temp = startTimes[id] + intValue(v.getAttribute("Weight"));
            if (temp > max[0]){
                max[0] = temp;
            }
        });
        finalTime = max[0];
    }
    protected static class GUIUpdater {
        IUpdatableState ui;
        public GUIUpdater(IUpdatableState ui) { this.ui = ui; }
        public void update(ISearchState searchState) {
            ui.updateWithState(searchState);
        }
    }

    @Override
    public void associateUI(IUpdatableState ui) {
        this.updater = new GUIUpdater(ui);
    }
}
