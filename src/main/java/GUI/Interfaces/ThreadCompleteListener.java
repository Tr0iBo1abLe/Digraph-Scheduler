package GUI.Interfaces;

/**
 * A sub interface specifies requirements of user-level threads (see #SolversThread and #SysInfoMonitoringThread)
 * @author Mason Shi
 */
public interface ThreadCompleteListener {
    void notifyOfSolversThreadComplete();
    void notifyOfSysInfoThreadUpdate();
}
