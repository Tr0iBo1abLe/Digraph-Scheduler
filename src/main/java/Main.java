import Exporter.GraphExporter;
import Parser.EdgeCtor;
import Parser.InputParser;
import Parser.VertexCtor;
import Solver.*;
import Solver.Interfaces.ISolver;
import lombok.NonNull;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;

import java.io.*;

public class Main {

    private Main(){
        //Ensure this class is not instantiated
    }

    public static void main(String[] args) {
        if(args.length != 1) {
            System.err.println("$0 takes 1 argument");
            return;
        }


        File inputFile = new File(args[0]);
        if(!inputFile.exists() || !inputFile.canRead()) {
            System.err.println("Can't open file");
        }

        Graph g = new DefaultGraph("g");
        FileSource fs = new FileSourceDOT();
        fs.addSink(g);
        try {
            fs.readAll(new BufferedInputStream(new FileInputStream("input3.dot")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        a(g);

        System.out.println(g.getEdgeSet());

        for(Node n:g.getNodeSet()) {
            System.out.println("Weight" + n.getAttribute("Weight"));
            System.out.println("BL" + n.getAttribute("BL"));
            System.out.println("index" + n.getIndex());
        }

        NewAStarSolver solver = new NewAStarSolver(g, 1);
        solver.doSolve();

        for(Node n:g.getNodeSet()) {
            System.out.println("Proc" + n.getAttribute("Processor"));
            System.out.println("Time" + n.getAttribute("ST"));
            System.out.println("index" + n.getIndex());
        }
    }

    public static void a(Graph g) {
        g.getNodeSet().forEach(v -> calculateBottomLevels(v, 0));
    }

    private static void calculateBottomLevels(@NonNull final Node v,
                                       final double level) {
        Double res;
        res = v.getAttribute("BL", Double.class);
        if(res == null) {
            v.setAttribute("BL", 0d);
            res = 0d;
        }
        if(res < level) {
            v.setAttribute("BL", level);
        }
        else {
            v.getEnteringEdgeSet().forEach(e -> {
                Node n = e.getSourceNode();
                calculateBottomLevels(n, level + v.getAttribute("Weight", Double.class));
            });
        }
    }


}

