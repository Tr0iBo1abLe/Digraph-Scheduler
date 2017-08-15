package GUI;

import lombok.Synchronized;
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
    }


    @Synchronized
    public void initializeLabels(Graph graph){
        SpriteManager sman = new SpriteManager(graph);
        Sprite header = sman.addSprite("header");
        header.addAttribute("ui.label", "Mouse LB: drag and move;  " +
                "Mouse Wheel: zoom in/out;  " +
                "Mouse RB: resize graph;  " +
                "Double-click: show/remove all attributes");
        header.setPosition(Units.PX, 0, 15, 0);
        int i = 0;
        graph.getNodeSet().stream().forEach(n -> {
            Sprite defaultAttr = sman.addSprite("idAndWeight"+n.getId());
            defaultAttr.addAttribute("ui.label",
                    "ID:" + n.getAttribute("id") +
                            " Wt:" + n.getAttribute("Weight"));
            defaultAttr.addAttribute("ui.style",
                    "text-alignment: center;\n" +
                            "\ttext-background-mode: rounded-box;\n" +
                            "\ttext-background-color: yellow;\n" +
                            "\ttext-size: 15px;\n");
            defaultAttr.attachToNode(n.getAttribute("id"));
            defaultAttr.setPosition(Units.PX,50,180,90);
            n.setAttribute("ui.label",
                    "\tProc:"+n.getAttribute("processor")+" \n"+
                            "\tSTime:"+n.getAttribute("startTime"));
            n.setAttribute("ui.style", "text-mode: hidden;");
                }
        );
        graph.getEdgeSet().stream().forEach(eg -> {
            eg.setAttribute("ui.label",
                            "Wt:"+eg.getAttribute("Weight"));
            eg.setAttribute("ui.style", "text-mode: hidden;");
                }
        );
    }

    @Synchronized
    public void colorNodes(Graph graph){
        graph.getNodeSet().stream().forEach(n -> {
            if ((n.getAttribute("processor")!=null)&&((int)n.getAttribute("processor")!=(-1))){

                double color = (double) (int) n.getAttribute("processor");

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

    @Synchronized
    public void updateNodes(Graph graph){
        graph.getNodeSet().stream().forEach(n -> n.setAttribute("ui.label",
                "\tProc:"+n.getAttribute("processor")+" \n"+
                        "\tSTime:"+n.getAttribute("startTime"))
        );
    }
}
