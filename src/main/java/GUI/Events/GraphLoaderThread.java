package GUI.Events;

import GUI.Frame.view.Controller;

import javax.swing.*;

public class GraphLoaderThread extends Thread {
    /**
     * Throws CloneNotSupportedException as a Thread can not be meaningfully
     * cloned. Construct a new Thread instead.
     *
     * @throws CloneNotSupportedException always
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
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
    public void run() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("test run");
            Controller.swingNode.setContent(Controller.viewPanel);
        });
    }
}
