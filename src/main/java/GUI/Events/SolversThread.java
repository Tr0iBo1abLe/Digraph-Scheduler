package GUI.Events;

import CommonInterface.ISolver;
import Exporter.GraphExporter;
import GUI.GraphViewer;
import GUI.Interfaces.ThreadCompleteListener;
import GUI.SwingMain;
import Graph.Edge;
import Graph.Vertex;
import lombok.Getter;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class SolversThread extends Thread {

    private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<>();

    @Getter
    private SwingMain swingMain; //Make sure to add listeners
    @Getter
    private ISolver solver;
    @Getter
    private GraphViewer graphViewer;
    @Getter
    private GraphExporter<Vertex, Edge<Vertex>> graphExporter;

    public SolversThread(SwingMain swingMain, ISolver iSolver, GraphViewer graphViewer, GraphExporter<Vertex, Edge<Vertex>> graphExporter){
        super();
        this.swingMain = swingMain;
        this.solver = iSolver;
        this.graphViewer = graphViewer;
        this.graphExporter = graphExporter;
    }

    public SolversThread(SwingMain swingMain, ISolver iSolver){
        super();
        this.swingMain = swingMain;
        this.solver = iSolver;
        this.graphViewer = null;
        this.graphExporter = null;
    }

    /**
     * Causes this thread to begin execution; the Java Virtual Machine
     * calls the <code>run</code> method of this thread.
     * <p>
     * The result is that two threads are running concurrently: the
     * current thread (which returns from the call to the
     * <code>start</code> method) and the other thread (which executes its
     * <code>run</code> method).
     * <p>
     * It is never legal to start a thread more than once.
     * In particular, a thread may not be restarted once it has completed
     * execution.
     *
     * @throws IllegalThreadStateException if the thread was already
     *                                     started.
     * @see #run()
     * @see #stop()
     */
    @Override
    public synchronized void start() {
        super.start();
    }

    /**
     * If this thread was constructed using a separate
     * <code>Runnable</code> run object, then that
     * <code>Runnable</code> object's <code>run</code> method is called;
     * otherwise, this method does nothing and returns.
     * <p>
     * Subclasses of <code>Thread</code> should override this method.
     *
     * @see #start()
     * @see #stop()
     */
    @Override
    public final void run() {
        try{
            doRun();
        } finally {
            notifyListeners();
        }
    }

    public void doRun(){
        solver.associateUI(swingMain);
        solver.doSolve();
    }

    public final void addListener(final ThreadCompleteListener listener) {
        listeners.add(listener);
    }

    public final void removeListener(final ThreadCompleteListener listener) {
        listeners.remove(listener);
    }

    private final void notifyListeners() {
        for (ThreadCompleteListener listener : listeners) {
            listener.notifyOfSolversThreadComplete();
        }
    }
}
