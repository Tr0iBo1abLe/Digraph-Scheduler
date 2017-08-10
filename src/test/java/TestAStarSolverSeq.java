import Exporter.GraphExporter;
import FileUtilities.FileUtils;
import Solver.AStarSolver;
import CommonInterface.ISolver;
import SolverOld.DFSSolver;
import Util.Helper;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkDOT;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


import java.io.*;

import static junit.framework.TestCase.assertEquals;

/**
 * Unit tests for the A* Solver implementation that's sequential (no parallel programming is tested here).
 *
 * Created by will on 7/31/17.
 */
public class TestAStarSolverSeq {

    private static int PROCESSOR_COUNT;
    private static final String TEST_FILES_PATH = "src/test/resources/TestSolver/";
    private Graph graph;
    private ISolver solver;

    @Before
    public void setup() {
        PROCESSOR_COUNT = 4;
        graph = new DefaultGraph("g");
    }

    @Ignore
    public void testStraightLine(){
        FileSource fs = new FileSourceDOT();
        fs.addSink(graph);
        try {
            fs.readAll(new BufferedInputStream(new FileInputStream(TEST_FILES_PATH+"input_straightline_4nodes.dot")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT); // Must construct solver only after graph has been parsed in.
        solver.doSolve();
        FileSink sink = new FileSinkDOT();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            sink.writeAll(graph, os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expected = FileUtils.readFileToString(TEST_FILES_PATH+"output_straightline_4nodes.dot");
        assertEquals(expected, os.toString());
    }

    /**
     * This test ensures multiple cores are being used when they are required for the optimal schedule as there are no
     * dependencies between nodes.
     */
    @Ignore
    public void test8Nodes0Edges(){
        PROCESSOR_COUNT = 8;
        FileSource fs = new FileSourceDOT();
        fs.addSink(graph);
        try {
            fs.readAll(new BufferedInputStream(new FileInputStream(TEST_FILES_PATH+"input_8nodes_0edges.dot")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Helper.finalise(graph);
        solver = new AStarSolver(graph, PROCESSOR_COUNT); // Must construct solver only after graph has been parsed in.
        solver.doSolve();
        FileSink sink = new FileSinkDOT();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            sink.writeAll(graph, os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expected = FileUtils.readFileToString(TEST_FILES_PATH+"output_8nodes_0edges.dot");
        assertEquals(expected, os.toString());
    }

}