package Solver.Parallel;

import Graph.Vertex;
import Solver.SearchState;
import lombok.Data;

/**
 * Created by e on 31/07/17.
 */
@Data
public class AStarTask implements Runnable {
    private final SearchState state;
    private final Vertex vertex;
    private final int processorId;

    @Override
    public void run() {

    }
}
