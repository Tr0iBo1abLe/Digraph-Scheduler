package GUI;

import org.graphstream.graph.Graph;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants.Units;
import org.graphstream.ui.view.ViewerListener;

import java.awt.event.*;
import java.util.Random;

public class GraphViewer extends Viewer{

    public GraphViewer(ProxyPipe source) { super(source); setHQ();}

    public GraphViewer(GraphicGraph graph) { super(graph); setHQ();}

    public GraphViewer(Graph graph, ThreadingModel threadingModel) { super(graph, threadingModel); setHQ();}

    private void setHQ(){
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        addHeader();
    }

    private void addHeader(){
        SpriteManager sman = new SpriteManager(graph);
        Sprite header = sman.addSprite("header");
        header.addAttribute("ui.label", "Mouse LB: drag and move;  " +
                "Mouse Wheel: zoom in/out;  " +
                "Mouse RB: resize graph;  " +
                "Double-click: show/remove all attributes");
        header.setPosition(Units.PX, 0, 15, 0);
    }

    public void colorNodes(Graph graph){
        graph.getNodeSet().stream().forEach(n -> {
            if ((n.getAttribute("Processor")!=null)&&((int)n.getAttribute("Processor")!=(-1))){

                double color = (double) (int) n.getAttribute("Processor");

                if (color < 10.00){
                    color = color*(0.1);
                    n.setAttribute("ui.color", color);
//                    System.out.println(color);
                    return;
                }
                while (color >= 10.00){
                    color = color*(0.5);
                }
                color = color*(0.1);
                n.setAttribute("ui.color", color);
            }
        });
    }
}
