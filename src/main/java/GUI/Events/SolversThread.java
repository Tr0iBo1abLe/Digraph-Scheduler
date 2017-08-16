package GUI.Events;

import CommonInterface.ISolver;
import Exporter.GraphExporter;
import GUI.GraphViewer;
import Graph.Edge;
import Graph.Vertex;
import lombok.Getter;

public class SolversThread extends Thread {
    @Getter
    private ISolver iSolver;
    @Getter
    private GraphViewer graphViewer;
    @Getter
    private GraphExporter<Vertex, Edge<Vertex>> graphExporter;

    public SolversThread(ISolver iSolver, GraphViewer graphViewer, GraphExporter<Vertex, Edge<Vertex>> graphExporter){
        super();
        this.iSolver = iSolver;
        this.graphViewer = graphViewer;
        this.graphExporter = graphExporter;
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
     * @see #Thread(ThreadGroup, Runnable, String)
     */
    @Override
    public void run() {
        super.run();
    }
}
