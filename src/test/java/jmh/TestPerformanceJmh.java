package jmh;

import org.openjdk.jmh.Main;
import org.openjdk.jmh.logic.results.Result;
import org.openjdk.jmh.logic.results.RunResult;
import org.openjdk.jmh.output.OutputFormatType;
import org.openjdk.jmh.profile.ProfilerType;
import org.openjdk.jmh.runner.BenchmarkRecord;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Map;

/**
 * Created by e on 5/08/17.
 */
public class TestPerformanceJmh {
    public static void main(String[] args) throws RunnerException {
        Options opts = new OptionsBuilder()
                .include(".*")
                .warmupIterations(10)
                .measurementIterations(15)
                .jvmArgs("-Xmx2000M")
                .forks(1)
                .outputFormat(OutputFormatType.TextReport)
                .addProfiler(ProfilerType.HS_GC)
                .addProfiler(ProfilerType.HS_THREAD)
                .build();

        Map<BenchmarkRecord,RunResult> records = new Runner(opts).run();
        for (Map.Entry<BenchmarkRecord, RunResult> result : records.entrySet()) {
            Result r = result.getValue().getPrimaryResult();
            System.out.println("API replied benchmark score: "
                    + r.getScore() + " "
                    + r.getScoreUnit() + " over "
                    + r.getStatistics().getN() + " iterations");
        }
    }


}
