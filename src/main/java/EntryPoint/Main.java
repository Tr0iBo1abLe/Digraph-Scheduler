package EntryPoint;

import CommonInterface.ISolver;
import Exporter.GraphExporter;
import GUI.SwingMain;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Solver.AStarSolver;
import Solver.AStarSolverPar;
import Solver.DFSSolver;
import Util.Helper;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import javax.swing.*;
import java.io.*;
import java.util.Arrays;

public class Main {

    private Main() {
        //Ensure this class is not instantiated
    }

    private enum Algo{AS, BNB}
        /*
         * Now this is the place where namespaces come in handy!
         * Sadly, we are in Java
         */

    private static void callSolver(File file, int procN, int parN, Algo algo, OutputStream os) {
        Graph<Vertex, EdgeWithCost<Vertex>> graph = Helper.fileToGraph(file);
        ISolver solver = null;
        switch(algo) {
            case AS:
                if (parN != 1) {
                    solver = new AStarSolverPar(graph, procN);
                } else {
                    solver = new AStarSolver(graph, procN);
                }
                break;
            case BNB:
                if (true) { // Change this when parallel is done
                    solver = new DFSSolver(graph, procN);
                }
                break;
        }
        solver.doSolve();

        final GraphExporter<Vertex, EdgeWithCost<Vertex>> vertexEdgeWithCostGraphExporter;
        vertexEdgeWithCostGraphExporter = new GraphExporter<Vertex, EdgeWithCost<Vertex>>();
        vertexEdgeWithCostGraphExporter.doExport(graph, new BufferedWriter(new OutputStreamWriter(os)));
    }

    public static void main(String[] args) {
        Namespace ns = null;
        ArgumentParser argumentParser = ArgumentParsers.newArgumentParser("HDNW")
                .defaultHelp(true)
                .description("A GPU Scheduling program");
        /*
        argumentParser.addArgument("-l", "--library")
                .choices("gs", "old")
                .setDefault("gs")
                .required(false)
                .help("Choose library to use. gs -> Use graphstream library, old -> use old parser and datastructure");
                */
        argumentParser.addArgument("-g", "--gui")
                .action(Arguments.storeTrue())
                .help("Choose whether to use GUI(Not implemented at the moment)");
        argumentParser.addArgument("-a", "--algorithm")
                .choices("as", "bnb")
                .setDefault("as")
                .required(false)
                .help("Choose the algorithm to use");
        argumentParser.addArgument("-p", "--processors")
                .metavar("N")
                .required(true)
                .type(Integer.class)
                .nargs(1)
                .help("Processor count");
        argumentParser.addArgument("-r", "--parallel")
                .metavar("M")
                .type(Integer.class)
                .nargs(1)
                .setDefault(Arrays.asList(new Integer[]{1}))
                .required(false)
                .help("Use parallel processing");
        argumentParser.addArgument("infile")
                .metavar("INFILENAME")
                .nargs(1)
                .required(true)
                .help("Filename to process");
        argumentParser.addArgument("outfile")
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
        String fileName, outfileName;
        OutputStream os = null;
        boolean gui;
        Algo algo;

        gui = ns.getBoolean("gui");
        procN = (int) ns.getList("processors").get(0);
        parN = (int) ns.getList("parallel").get(0);
        fileName = (String) ns.getList("infile").get(0);
        String outfile = ns.getString("outfile");
        String algoStr = ns.getString("algorithm");
        if(algoStr.matches("as")) algo = Algo.AS;
        else algo = Algo.BNB;
        if (outfile == null) {
            os = new BufferedOutputStream(System.out);
        } else {
            try {
                os = new FileOutputStream(new File(outfile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        File inputFile = new File(fileName);
        if (!inputFile.exists() || !inputFile.canRead()) {
            System.err.println("Can't open file");
        }

        if (gui) {
            Graph<Vertex, EdgeWithCost<Vertex>> graph = Helper.fileToGraph(inputFile);
            ISolver solver = new Solver.AStarSolver(graph, procN);
            SwingMain.init(graph, solver);
            SwingUtilities.invokeLater(new SwingMain());
        } else {
            callSolver(inputFile, procN, parN, algo, os);
        }
    }
}

