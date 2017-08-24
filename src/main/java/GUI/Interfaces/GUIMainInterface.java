package GUI.Interfaces;

import CommonInterface.ISearchState;
import GUI.Events.SolversThread;
import GUI.Events.SysInfoMonitoringThread;
import GUI.Models.SysInfoModel;
import Solver.AbstractSolver;

/**
 * An integrated interface for GUI controller.
 * This specifies essential methods for controller in order to update GUI using information that comes from other modules
 * @Author Mason Shi
 */
public interface GUIMainInterface extends IUpdatableState, ThreadCompleteListener {

    @Override
    void updateWithState(ISearchState searchState, AbstractSolver Solver);

    @Override
    void notifyOfSolversThreadComplete();

    @Override
    void notifyOfSysInfoThreadUpdate();

    void updateSysInfo(SysInfoModel sysInfoModel);
}
