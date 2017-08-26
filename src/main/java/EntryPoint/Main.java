package EntryPoint;

import CommonInterface.ISolver;
import Exporter.GraphExporter;
import GUI.GUIMain;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Solver.SolverFactory;
import Util.Helper;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.*;
import java.util.Collections;

public final class Main {

    private Main() {
        //Ensure this class is not instantiated
    }

    private static void callSolver(File file, int procN, int parN, OutputStream os) {
        Graph<Vertex, EdgeWithCost<Vertex>> graph = Helper.fileToGraph(file);
        ISolver solver = new SolverFactory(graph, procN, parN).createSolver();
        solver.doSolveAndCompleteSchedule();

        final GraphExporter<Vertex, EdgeWithCost<Vertex>> vertexEdgeWithCostGraphExporter;
        vertexEdgeWithCostGraphExporter = new GraphExporter<Vertex, EdgeWithCost<Vertex>>();
        vertexEdgeWithCostGraphExporter.doExport(graph, new BufferedWriter(new OutputStreamWriter(os)));
    }

    public static void main(String[] args) {
        org.apache.log4j.BasicConfigurator.configure();
        Namespace ns = null;
        ArgumentParser argumentParser = ArgumentParsers.newArgumentParser("Scheduler")
                .defaultHelp(true)
                .description("A GPU Scheduling program");
        argumentParser.addArgument("infile")
                .metavar("INFILENAME")
                .nargs(1)
                .required(true)
                .help("Filename to process");
        argumentParser.addArgument("processors")
                .metavar("P")
                .required(true)
                .type(Integer.class)
                .nargs(1)
                .help("Processor count");
        argumentParser.addArgument("-v")
                .action(Arguments.storeTrue())
                .help("Choose whether to use GUI");
        argumentParser.addArgument("-p", "--parallel")
                .metavar("M")
                .type(Integer.class)
                .nargs(1)
                .setDefault(Collections.singletonList(1))
                .required(false)
                .help("Use parallel processing");
        argumentParser.addArgument("-o")
                .metavar("OUTFILENAME")
                .nargs("?")
                .required(false)
                .help("Output file name, write to STDOUT if non-specified");

        try {
            ns = argumentParser.parseArgs(args);
        } catch (ArgumentParserException e) {
            argumentParser.handleError(e);
            System.exit(1);
        }

        int procN, parN;
        String fileName;
        OutputStream os = null;
        boolean gui;

        gui = ns.getBoolean("v");
        procN = (int) ns.getList("processors").get(0);
        parN = (int) ns.getList("parallel").get(0);
        fileName = (String) ns.getList("infile").get(0);
        String outfile = ns.getString("outfile");

        if (outfile == null) {
            os = new BufferedOutputStream(System.out);
        } else {
            try {
                File file = new File(outfile);
                os = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        File inputFile = new File(fileName);
        if (!inputFile.exists() || !inputFile.canRead()) {
            System.err.println("Can't open file");
        }

        Graph<Vertex, EdgeWithCost<Vertex>> graph = Helper.fileToGraph(inputFile);
        ISolver solver = new SolverFactory(graph, procN, parN).createSolver();
        if (gui) {
            GUIMain.init(graph, solver);
            new GUIMain().run();
        }
        callSolver(inputFile, procN, parN, os);
    }
}

