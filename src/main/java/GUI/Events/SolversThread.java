package GUI.Events;

import CommonInterface.ISolver;
import GUI.Frame.view.Controller;
import GUI.Interfaces.ThreadCompleteListener;
import lombok.Getter;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * A user-level thread as a complementary replacement for SwingWorker in order to support Start, Pause feature (See #Controller).
 * It fires an notification to Controller once Solver safely finishes.
 * This class is not a part of parallelization it is simply a thread representing the particular connection
 * between solvers and GUI interfaces (see #Controller, #GraphViewer, and #ScheduleChart).
 * It is one of the util threads managed by Controller (see #Controller and #Start button).
 * @author Mason Shi
 */
public class SolversThread extends Thread {

    private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<>();

    public static boolean isStoped = false;
    @Getter
    private Controller controller; //Make sure to add listeners
    @Getter
    private ISolver solver;

    public SolversThread(Controller controller, ISolver iSolver){
        super();
        this.controller = controller;
        this.solver = iSolver;
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
        solver.associateUI(controller);
        solver.doSolveAndCompleteSchedule();
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
