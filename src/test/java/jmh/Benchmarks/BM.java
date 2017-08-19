package jmh.Benchmarks;

import EntryPoint.Main;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;

/**
 * Created by e on 5/08/17.
 */
public class BM {

    public static final int PROC_N = 2;
    public static final String FN = "input4.dot";

    @GenerateMicroBenchmark
    public void bench() {
        Main.main(new String[]
                {
                        "-a", "as",
                        "-p", String.valueOf(PROC_N),
                        "-r", "1",
                        FN});
    }

    @GenerateMicroBenchmark
    public void benchPar() {
        Main.main(new String[]
                {
                        "-a", "as",
                        "-p", String.valueOf(PROC_N),
                        "-r", "4",
                        FN});
    }
}
