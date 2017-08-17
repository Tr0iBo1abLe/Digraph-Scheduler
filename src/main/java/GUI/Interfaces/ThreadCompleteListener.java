package GUI.Interfaces;

import GUI.Events.SolversThread;
import GUI.Events.SysInfoMonitoringThread;

public interface ThreadCompleteListener {
    void notifyOfSolversThreadComplete();
    void notifyOfSysInfoThreadUpdate();
}
