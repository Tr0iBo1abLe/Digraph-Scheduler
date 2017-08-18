package GUI.Models;

import lombok.Getter;
import org.hyperic.sigar.*;

import java.io.File;
import java.io.IOException;

public class SysInfoModel {

    private final Sigar sigar;
    private static SysInfoModel sysInfoModel = null;

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

    public static SysInfoModel getInstance(){
        if (sysInfoModel == null){
            sysInfoModel = new SysInfoModel();
            return sysInfoModel;
        }
        return sysInfoModel;
    }

    private SysInfoModel(){
        String currentLibPath = System.getProperty("java.library.path");
        File currentDirFile = new File(".");
        String helper = currentDirFile.getAbsolutePath();
        String newLibPath = currentLibPath + ":" + helper + "/sigar-bin/slib" + ":" + helper;
        System.setProperty("java.library.path", newLibPath);
        sigar = new Sigar();
        mem = null;
        cpuPerc = null;
        try {
            cpuList = sigar.getCpuList();
            cpuCount = cpuList.length;
            mem = sigar.getMem();
            totalMem = mem.getTotal();
            totalMemInMByte = totalMem/1024l/1024l;
        } catch (SigarException e) {
            e.printStackTrace();
        }
        update();
    }

    public void update(){
        try {
            mem = sigar.getMem();
            cpuPerc = sigar.getCpuPerc(); //NaN can be returned by this call, GUI needs to handle String 'NaN' if necessary
            double temp = cpuPerc.getCombined()*100d;
            if (temp != Double.NaN){
                cpuPercentage = temp;
            }
        } catch (SigarException se) {
            se.printStackTrace();
        }
    }
}
