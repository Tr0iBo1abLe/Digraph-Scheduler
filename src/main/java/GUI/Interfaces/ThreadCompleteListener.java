package GUI.Interfaces;

import GUI.Events.SolversThread;
import GUI.Events.SysInfoMonitoringThread;

/**
 * A sub interface specifies requirements of user-level threads (see #SolversThread and #SysInfoMonitoringThread)
 * @author Mason Shi
 */
public interface ThreadCompleteListener {
    void notifyOfSolversThreadComplete();
    void notifyOfSysInfoThreadUpdate();
}
