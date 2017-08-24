package GUI.Events;


import GUI.Interfaces.ThreadCompleteListener;
import GUI.Models.SysInfoModel;
import javafx.application.Platform;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * A timer, essentially, it keeps firing 'events' to request Controller to go over to SysInfoModel and acquire the latest sys info.
 * @author Mason Shi
 */
public class SysInfoMonitoringThread extends Thread{

    //Make sure to add listeners
    private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<>();

    private Timer timer;

    @lombok.Getter
    private SysInfoModel sysInfoModel;

    public SysInfoMonitoringThread (SysInfoModel sysInfoModel){
        super();
        this.sysInfoModel = sysInfoModel;
        timer = new Timer();
    }

    public final void addListener(final ThreadCompleteListener listener) {
        listeners.add(listener);
    }

    public final void removeListener(final ThreadCompleteListener listener) {
        listeners.remove(listener);
    }

    private final void notifyListeners() {
        for (ThreadCompleteListener listener : listeners) {
            listener.notifyOfSysInfoThreadUpdate();
        }
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
        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                          Platform.runLater(() ->
                                          {
                                              try {
                                                  sysInfoModel.update();
                                              } finally {
                                                  notifyListeners();
                                              }
                                          });
                                      }
                                  },
                100, 100);
    }

    /**
     * Terminate the run()
     * @Param long countdown - time to wait before cancelling the timer
     */
    public void stopTimer(long countdown){
        if (countdown == 0l){
            timer.cancel();
        }else{
            try {
                wait(countdown);
                timer.cancel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
