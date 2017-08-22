package GUI.Interfaces;

import CommonInterface.ISearchState;
import GUI.Events.SolversThread;
import GUI.Events.SysInfoMonitoringThread;
import GUI.Models.SysInfoModel;
import Solver.AbstractSolver;

public interface GUIMainInterface extends Runnable, IUpdatableState, ThreadCompleteListener {

    @Override
    void updateWithState(ISearchState searchState, AbstractSolver Solver);

    @Override
    void notifyOfSolversThreadComplete();

    @Override
    void notifyOfSysInfoThreadUpdate();

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    void run(); //TODO - DELETE THIS WHEN FINISHED AS GUIENTRY DOES NOT REQUIRE TO BE RUNNABLE BY ITSELF

    void updateSysInfo(SysInfoModel sysInfoModel);
}
