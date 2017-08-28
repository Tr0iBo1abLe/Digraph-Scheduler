package EntryPoint;

import CommonInterface.ISolver;
import Exporter.GraphExporter;
import GUI.GUIMain;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Solver.SolverFactory;
import Util.Helper;
import lombok.extern.log4j.Log4j;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.log4j.Level;
import org.apache.log4j.spi.Configurator;

import java.io.*;
import java.util.Collections;

@Log4j
public final class Main {

    //used by gui output because FX is blocking
    public static OutputStream os;

    private Main() {
        //Ensure this class is not instantiated
    }

    private static void callSolver(File file, int processorCount, int parallelProcessorCount, OutputStream os) {
        Graph<Vertex, EdgeWithCost<Vertex>> graph = Helper.fileToGraph(file);
        ISolver solver = new SolverFactory(graph, processorCount, parallelProcessorCount).createSolver();
        solver.doSolveAndCompleteSchedule();

        final GraphExporter<Vertex, EdgeWithCost<Vertex>> vertexEdgeWithCostGraphExporter;
        vertexEdgeWithCostGraphExporter = new GraphExporter<Vertex, EdgeWithCost<Vertex>>();
        vertexEdgeWithCostGraphExporter.doExport(graph, new BufferedWriter(new OutputStreamWriter(os)));
    }

    public static void main(String[] args) {
        org.apache.log4j.BasicConfigurator.configure();
        log.setLevel(Level.OFF);

        Namespace ns = null;

        ArgumentParser argumentParser = ArgumentParsers.newArgumentParser("Scheduler")
                .defaultHelp(false)
                .description("A GPU Scheduling program");
        argumentParser.usage("Scheduler.jar INPUT.dot P [-p N] [-v] [-o [OUTPUT]] [-h]");

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
                .setDefault("")
                .setConst("")
                .help("Output file name, write to STDOUT if non-specified");


        try {
            ns = argumentParser.parseArgs(args);
        } catch (ArgumentParserException e) {
            argumentParser.handleError(e);
            System.exit(1);
        }

        int procN, parN;
        String fileName;
        os = null;
        boolean gui;

        gui = ns.getBoolean("v");
        procN = (int) ns.getList("processors").get(0);
        parN = (int) ns.getList("parallel").get(0);
        fileName = (String) ns.getList("infile").get(0);
        String outfile = ns.getString("o");

        if (outfile == null) {
            os = new BufferedOutputStream(System.out);
        } else {
            File file;
            try {
                if (outfile.equals("")){
                    file = new File(fileName.substring(0, fileName.length() - 4) + "-output.dot");
                } else {
                    if (outfile.endsWith(".dot")) {
                        file = new File(outfile);
                    } else {
                        file = new File(outfile + ".dot");
                    }
                }
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
        } else {
            callSolver(inputFile, procN, parN, os);
        }
    }
}

