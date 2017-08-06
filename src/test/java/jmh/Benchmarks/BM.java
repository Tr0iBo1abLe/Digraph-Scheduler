package jmh.Benchmarks;

import EntryPoint.Main;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;

/**
 * Created by e on 5/08/17.
 */
public class BM {

    public static final int PROC_N = 2;
    public static final String FN = "input3.dot";

    @GenerateMicroBenchmark
    public void benchGS() {
        Main.main(new String[]
                {"-l", "gs",
                "-a", "as",
                "-p", String.valueOf(PROC_N),
                "-r", "1",
                FN});
    }
    @GenerateMicroBenchmark
    public void benchOld() {
        Main.main(new String[]
                {"-l", "old",
                "-a", "as",
                "-p", String.valueOf(PROC_N),
                "-r", "1",
                FN});
    }
    @GenerateMicroBenchmark
    public void benchGSPar() {
        Main.main(new String[]
                {"-l", "gs",
                "-a", "as",
                "-p", String.valueOf(PROC_N),
                "-r", "4",
                FN});
    }
    @GenerateMicroBenchmark
    public void benchOldPar() {
        Main.main(new String[]
                {"-l", "old",
                "-a", "as",
                "-p", String.valueOf(PROC_N),
                "-r", "4",
                FN});
    }
}
