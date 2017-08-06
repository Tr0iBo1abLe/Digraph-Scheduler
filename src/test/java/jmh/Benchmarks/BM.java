package jmh.Benchmarks;

import EntryPoint.Main;
import EntryPoint.MainOld;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;

/**
 * Created by e on 5/08/17.
 */
public class BM {

    @GenerateMicroBenchmark
    public void benchGS() {
        Main.main(new String[]{"input3.dot", "2"});
    }

    @GenerateMicroBenchmark
    public void benchOld() {
        MainOld.main(new String[]{"input3.dot", "2"});
    }
    @GenerateMicroBenchmark
    public void benchGSPar() {
        Main.main(new String[]{"input3.dot", "2", "-p"});
    }
    @GenerateMicroBenchmark
    public void benchOldPar() {
        MainOld.main(new String[]{"input3.dot", "2", "-p"});
    }
}
