package Solver;

import CommonInterface.ISolver;
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

        for(;;) {
            SearchState currentBestSchedule = queue.remove();

            long remMem = Helper.getRemainingMemory();
            log.debug("Checking remaining memory: Remaining -> " + remMem);
            if(remMem <= 600_000_000L) { // The memory value should be fine tuned a bit more
                /*      ^GB ^MB ^kB    */
                log.debug("Calling DFSSolver");
                if(guiTimer != null) guiTimer.cancel();

                DFSSolver nextSolver = new DFSSolver(getGraph(), getProcessorCount(), currentBestSchedule);
                queue.clear();
                nextSolver.setUpdater(getUpdater());
                System.gc();
                currentBestSchedule = nextSolver.continueSolve();
            }

            if (currentBestSchedule.getNumVertices() == graph.getVertices().size()) {
                // We have found THE optimal solution
                scheduleVertices(currentBestSchedule);
                if (updater != null && guiTimer != null) {
                    updater.update(currentBestSchedule);
                    guiTimer.cancel();
                }
                return;
            }

            for (Vertex vertex : currentBestSchedule.getLegalVertices()) {
                for (int processorID = 0; processorID < processorCount; processorID++) {
                    SearchState nextSearchState = new SearchState(currentBestSchedule, vertex, processorID);
                    if (!queue.contains(nextSearchState)) {
                        queue.add(nextSearchState);
                    }
                }
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
