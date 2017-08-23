package Solver;

import Datastructure.FastPriorityQueue;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import lombok.Getter;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public final class AStarSolver extends AbstractSolver {

    private final Queue<SearchState> queue;

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
            isUpdatableProgressBar = true;
            AbstractSolver solver = this; //provide a reference to GUI classes
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                                          @Override
                                          public void run() {
                                              updater.update(queue.peek(), solver);
                                          }
                                      },
                    100, 100);
        }

        queue.add(new SearchState());

        while (true) {
            SearchState currentBestSchedule = queue.remove();

            if (currentBestSchedule.getNumVertices() == graph.getVertices().size()) {
                // We have found THE optimal solution
                scheduleVertices(currentBestSchedule);
                if (updater != null && timer != null) {
                    updater.update(currentBestSchedule, this);
                    timer.cancel();
                }
                return;
            }

            for (Vertex vertex : currentBestSchedule.getLegalVertices()) {
                for (int processorID = 0; processorID < processorCount; processorID++) {
                    SearchState nextSearchState = new SearchState(currentBestSchedule, vertex, processorID);
                    if (!queue.contains(nextSearchState)) {
                        queue.add(nextSearchState);
                    }
                    //increase the state counter for GUI, process only when there is a GUI to update
                    if (isUpdatableProgressBar){ //true if there is a GUI progress bar needs to be updated
                        stateCounter++;
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
