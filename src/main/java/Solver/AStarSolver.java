package Solver;

import Datastructure.FastPriorityQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Util.Helper;
import lombok.extern.log4j.Log4j;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

@Log4j
public final class AStarSolver extends AbstractSolver {

    private final Queue<SearchState> queue;
    private Timer guiTimer;

    public AStarSolver(Graph<Vertex, EdgeWithCost<Vertex>> graph, int processorCount) {
        super(graph, processorCount);
        queue = new FastPriorityQueue<>();
    }

    @Override
    public void doSolve() {
        /* This method is blocking, we need a way to notify the GUI */
        SearchState.initialise(graph);
        if (updater != null) {
            /* We have an updater and a UI to update */
            guiTimer = new Timer();
            guiTimer.scheduleAtFixedRate(new TimerTask() {
                                             @Override
                                             public void run() {
                                                 updater.update(queue.peek());
                                             }
                                         },
                    100, 100);
        }

        queue.add(new SearchState());
        for (; ; ) {
            SearchState currBestState = queue.remove();

            long remMem = Helper.getRemainingMemory();
        //    log.debug("Checking remaining memory: Remaining -> " + remMem);
            log.debug("Queue Size " + queue.size() + ", State size " + currBestState.getNumVertices());
            if (remMem <= 600_000_000L) { // The memory value should be fine tuned a bit more
                /*      ^GB ^MB ^kB    */
                currBestState = continueSolveWithBnB(currBestState);
            }

            if (currBestState.getNumVertices() == graph.getVertices().size()) {
                // We have found THE optimal solution
                scheduleVertices(currBestState);
                if (updater != null && guiTimer != null) {
                    updater.update(currBestState);
                    guiTimer.cancel();
                }
                return;
            }

            for (Vertex vertex : currBestState.getLegalVertices()) {
                for (int processorID = 0; processorID < processorCount; processorID++) {
                    SearchState nextSearchState = new SearchState(currBestState, vertex, processorID);
                    if (!queue.contains(nextSearchState)) {
                        queue.add(nextSearchState);
                    }
                }
            }
        }
    }

    private SearchState continueSolveWithBnB(SearchState currBestState) {
        if (guiTimer != null) guiTimer.cancel();
        log.debug("Calling DFSSolver");
        DFSSolver nextSolver = new DFSSolver(getGraph(), getProcessorCount(), currBestState);
        queue.clear();
        nextSolver.setUpdater(getUpdater());
        System.gc();
        return nextSolver.continueSolve();
    }
}
