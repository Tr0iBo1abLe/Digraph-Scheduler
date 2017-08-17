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
    private Mem mem;
    @Getter
    private Cpu[] cpuList;

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
        String newLibPath = currentLibPath + ":" + helper + "/sigar-bin/slib";
        System.setProperty("java.library.path", newLibPath);
        sigar = new Sigar();
        mem = null;
        cpuPerc = null;
        cpuList = null;
        update();
    }

    public void update(){
        try {
            mem = sigar.getMem();
            cpuPerc = sigar.getCpuPerc();
            cpuList = sigar.getCpuList();
        } catch (SigarException se) {
            se.printStackTrace();
        }


        System.out.print(mem.getUsedPercent()+"\t");
        System.out.print((cpuPerc.getCombined()*100)+"\t");
        System.out.print(cpuList.length+"\n");
    }
}
