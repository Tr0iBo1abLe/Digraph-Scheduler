package GUI;

import CommonInterface.ISolver;
import GUI.Frame.GUIEntry;
import GUI.Frame.view.Controller;
import GUI.Frame.view.GraphCSS;
import GUI.Util.ColorManager;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Util.Helper;
import cern.clhep.Units;
import com.alee.utils.FileUtils;
import javafx.application.Application;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static cern.clhep.Units.s;


/**
 * This class represents the interception between algorithm module and GUI module.
 * It wraps up the JavaFX entry point in order to be recognized by Main entry and also provides a certain degree of isolation between
 * GUI modules and non-GUI modules.
 *
 * @author Mason Shi
 */
public class GUIMain implements Runnable {

    public static String inputFileName;
    public static String algorithmType;

    // static init to feed necessary information to JavaFX entry point.
    public static void init(Graph<? extends Vertex, ? extends EdgeWithCost> graph, ISolver solveri, String inputFileName, String algorithmType) {
        GUIMain.inputFileName = inputFileName;
        GUIMain.algorithmType = algorithmType;
        Controller.graph = graph;
        Controller.visualGraph = Helper.convertToGsGraph(graph);
        Controller.solver = solveri;
        Controller.visualGraph.addAttribute("ui.stylesheet", GraphCSS.GRAPH_CSS);
        ColorManager.generateColor(solveri.getProcessorCount());
    }

    @Override
    public void run() {
        Application.launch(GUIEntry.class);
    }

}
