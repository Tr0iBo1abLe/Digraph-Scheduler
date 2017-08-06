import Util.Helper;

import Solver.AStarSolver;
import Solver.AStarSolverPar;
import Solver.Interfaces.ISolver;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkDOT;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import org.junit.Before;
import org.junit.Test;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.State;

import java.io.*;

/**
 * Created by e on 1/08/17.
 */
public class TestPerformance {

    private static final int PROCESSOR_COUNT = 2;
    private static final String TEST_FILES_PATH = "src/test/resources/TestSolver/";
    public static final String FILE_NAME = "input3.dot";
    private Graph graph;

    @Before
    public void setup(){
        graph = new DefaultGraph("g");
        FileSource fs = new FileSourceDOT();
        fs.addSink(graph);
        try {
            fs.readAll(new BufferedInputStream(new FileInputStream(FILE_NAME)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Helper.finalise(graph);
    }

    @Test
    public void testSeq(){
        long t = System.currentTimeMillis();
        ISolver solver = new AStarSolver(graph, 2);
        solver.doSolve();
        FileSink sink = new FileSinkDOT();
        try {
            sink.writeAll(graph, new BufferedOutputStream(System.err));
        } catch (IOException e) {
            e.printStackTrace();
        }
        long delta = System.currentTimeMillis() - t;
        System.out.println("Seq Astar Time taken = " + delta);
    }

    @Test
    public void testPar(){
        long t = System.currentTimeMillis();
        ISolver solver = new AStarSolverPar(graph, 2);
        solver.doSolve();
        FileSink sink = new FileSinkDOT();
        try {
            sink.writeAll(graph, new BufferedOutputStream(System.err));
            sink.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long delta = System.currentTimeMillis() - t;
        System.err.println("Par Astar Time taken = " + delta);
    }

}
