package GUI.Models;

import GUI.Util.NativeLoader;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.hyperic.sigar.*;

/**
 * A model class with respect to MVC pattern.
 * It monitors the operating system's environment and
 * stores system info fields and methods. This is required by Controller to obtain system info.
 *
 * @author Mason Shi
 */
@Log4j
public class SysInfoModel {

    private static SysInfoModel sysInfoModel = null;
    private final Sigar sigar;
    @Getter
    private CpuPerc cpuPerc;
    @Getter
    private double cpuPercentage = 0d;
    @Getter
    private Mem mem;
    @Getter
    private Cpu[] cpuList;
    @Getter
    private int cpuCount;
    @Getter
    private long totalMem;
    @Getter
    private long totalMemInMByte;

    private SysInfoModel() {
        log.info("found sigar lib path at: " + this.getClass().getClassLoader().getResource("sigar-bin/slib").getPath());
        NativeLoader nativeLoader = new NativeLoader();
        nativeLoader.loadLibrary();
        sigar = new Sigar();
        mem = null;
        cpuPerc = null;
        try {
            cpuList = sigar.getCpuList();
            cpuCount = cpuList.length;
            mem = sigar.getMem();
            totalMem = mem.getTotal();
            totalMemInMByte = totalMem / 1024L / 1024L;
        } catch (SigarException e) {
            e.printStackTrace();
        }
        update();
    }

    public static SysInfoModel getInstance() {
        if (sysInfoModel == null) {
            sysInfoModel = new SysInfoModel();
            return sysInfoModel;
        }
        return sysInfoModel;
    }

    public void update() {
        try {
            mem = sigar.getMem();
            cpuPerc = sigar.getCpuPerc(); //NaN can be returned by this call, GUI needs to handle String 'NaN' if necessary
            double temp = cpuPerc.getCombined() * 100d;
            if (temp != Double.NaN) {
                cpuPercentage = temp;
            }
        } catch (SigarException se) {
            se.printStackTrace();
        }
    }
}
