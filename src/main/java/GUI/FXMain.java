package GUI;

import CommonInterface.ISearchState;
import CommonInterface.ISolver;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Solver.SearchState;
import Util.Helper;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.Timer;
import java.util.stream.IntStream;

/**
 * Created by e on 7/08/17.
 */
public class FXMain extends Application {
    @FXML
    private SwingNode graphNode;
    @FXML
    private XYChart<?, ?> xyChart; // raw type for now
    @FXML
    private GridPane gridPane;
    @FXML
    private Button startButton;


    public FXMain() {
        if(visualGraph == null)
            throw new RuntimeException(getClass().getSimpleName() + " must be init()'d.");
    }

    @FXML
    public void onStartClick() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               updateVisualGraph(solver.pollState());
                           }
                       }
                , 500);
        solver.doSolve();

    }

    @Override
    public void start(Stage stage) throws Exception {
        if(visualGraph == null)
            throw new RuntimeException(getClass().getSimpleName() + " must be init()'d before calling run.");
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(new BufferedInputStream(new FileInputStream("src/main/java/GUI/FXMain.fxml")));
        SwingNode swingNode = new SwingNode();
        makeSwingNode(swingNode);
        swingNode.setVisible(true);
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    private static void makeSwingNode(final SwingNode swingNode)  {
        SwingUtilities.invokeLater(() ->
                swingNode.setContent(viewPanel));
    }

    private static ISolver solver;
    private static org.graphstream.graph.Graph visualGraph = null;
    private static Viewer viewer = null;
    private static ViewPanel viewPanel = null;
    private static ViewerPipe viewerPipe = null;

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
}
