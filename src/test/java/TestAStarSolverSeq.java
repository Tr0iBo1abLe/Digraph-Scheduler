import FileUtilities.FileUtils;
import Solver.AStarSolver;
import Solver.Interfaces.ISolver;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkDOT;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;
import org.junit.Before;
import org.junit.Test;


import java.io.*;

import static junit.framework.TestCase.assertEquals;

/**
 * Unit tests for the A* Solver implementation that's sequential (no parallel programming is tested here).
 *
 * Created by will on 7/31/17.
 */
public class TestAStarSolverSeq {

    private static final int PROCESSOR_COUNT = 4;
    private static final String TEST_FILES_PATH = "src/test/resources/TestSolver/";
    private Graph graph;
    private ISolver solver;

    @Before
    public void setup() {
        graph = new DefaultGraph("g");
        FileSource fs = new FileSourceDOT();
        fs.addSink(graph);
        try {
            fs.readAll(new BufferedInputStream(new FileInputStream(TEST_FILES_PATH+"input_straightline_4nodes.dot")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Helper.finalise(graph);
    }

    @Test
    public void testStraightLine(){
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

}