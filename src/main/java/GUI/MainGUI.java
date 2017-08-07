package GUI;

import CommonInterface.ISearchState;
import CommonInterface.ISolver;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Util.Helper;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import javax.swing.*;
import java.util.stream.IntStream;

/**
 * Created by e on 7/08/17.
 */
public class MainGUI implements Runnable {
    private static ISolver solver;
    private static org.graphstream.graph.Graph visualGraph = null;
    private static Viewer viewer = null;
    private static ViewPanel viewPanel = null;
    private static ViewerPipe viewerPipe = null;
    private static JFrame rootFrame;

    public static final String STYLE_RESORUCE = "url('style.css')";

    public static void init(org.graphstream.graph.Graph graph, ISolver solveri) {
        visualGraph = graph;
        solver = solveri;
        initRest();
    }

    public static void init(Graph<? extends Vertex, ? extends EdgeWithCost> graph, ISolver solveri) {
        visualGraph = Helper.convertToGsGraph(graph);
        solver = solveri;
        initRest();
    }

    private static void initRest() {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        viewer = new Viewer(visualGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        viewerPipe = viewer.newViewerPipe();
        viewPanel = viewer.addDefaultView(false);
        visualGraph.addAttribute("ui.stylesheet", STYLE_RESORUCE);
    }

    public static void updateVisualGraph(ISearchState searchState) {
        int[] processors = searchState.getProcessors();
        int[] startTimes = searchState.getStartTimes();
        IntStream.of(0, visualGraph.getNodeSet().size()-1).forEach(i -> {
        });
        visualGraph.getNodeSet().forEach(n -> {
            int index = n.getIndex();
            if (processors[index] != -1) {
                n.addAttribute("ui.class", "sched");
                n.addAttribute("processor", processors[index]+1);
                n.addAttribute("startTime", startTimes[index]);
            }
        });
    }


    @Override
    public void run() {

    }
}
