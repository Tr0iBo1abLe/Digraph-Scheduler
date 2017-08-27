package GUI;

import CommonInterface.ISolver;
import GUI.Frame.GUIEntry;
import GUI.Frame.view.Controller;
import GUI.Util.ColorManager;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Util.Helper;
import javafx.application.Application;


/**
 * This class represents the interception between algorithm module and GUI module.
 * It wraps up the JavaFX entry point in order to be recognized by Main entry and also provides a certain degree of isolation between
 * GUI modules and non-GUI modules.
 *
 * @author Mason Shi
 */
public class GUIMain implements Runnable {

    // static init to feed necessary information to JavaFX entry point.
    public static void init(Graph<? extends Vertex, ? extends EdgeWithCost> graph, ISolver solveri) {
        Controller.graph = graph;
        Controller.visualGraph = Helper.convertToGsGraph(graph);
        Controller.visualGraph.addAttribute("ui.stylesheet", "url('target/classes/GUI/Frame/view/Graph.css')");
        Controller.solver = solveri;
        ColorManager.generateColor(solveri.getProcessorCount());
    }

    @Override
    public void run() {
        Application.launch(GUIEntry.class);
    }

}
