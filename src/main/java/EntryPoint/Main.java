package EntryPoint;

import FileUtilities.FileSinkSpecialDot;
import Solver.*;
import Solver.Interfaces.ISolver;
import Util.Helper;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;

import java.io.*;

public class Main {

    private Main(){
        //Ensure this class is not instantiated
    }

    public static void main(String[] args) {
        boolean par = false;
        int procN = 1;
        if(args.length < 1) {
            System.err.println("$0 takes 1 required argument(s)");
            return;
        }

        if(args.length >= 2) {
            procN = Integer.parseInt(args[1]);
        }

        if(args.length >= 3 && args[1].matches("[-]p")) {
            par = true;
        }

        File inputFile = new File(args[0]);
        if(!inputFile.exists() || !inputFile.canRead()) {
            System.err.println("Can't open file");
        }


        Graph g = new DefaultGraph("g");
        FileSource fs = new FileSourceDOT();
        fs.addSink(g);
        try {
            fs.readAll(new BufferedInputStream(new FileInputStream(inputFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Helper.finalise(g);

        System.out.println(g.getEdgeSet());

        for(Node n:g.getNodeSet()) {
            System.out.println("Weight" + n.getAttribute("Weight"));
            System.out.println("BL" + n.getAttribute("BL"));
            System.out.println("index" + n.getIndex());
        }

        ISolver solver = new AStarSolver(g, 2);
        if(par) {
            solver = new AStarSolverPar(g, procN);
        }
        else {
            solver = new AStarSolver(g, procN);
        }
        solver.doSolve();
        

        Helper.stripUneeded(g);

        for(Node n:g.getNodeSet()) {
            System.out.println("Proc" + n.getAttribute("Processor"));
            System.out.println("Time" + n.getAttribute("ST"));
            System.out.println("index" + n.getIndex());
        }
        FileSink sink = new FileSinkSpecialDot("88");
        try {
            sink.writeAll(g, new BufferedOutputStream(System.out));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

