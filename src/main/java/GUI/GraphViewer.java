package GUI;

import lombok.Getter;
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
import java.util.*;

public class GraphViewer extends Viewer{

    @Getter
    private Map<String, String> colorSet = new HashMap<String, String>(); //A mapping between rgb colors and processor ids

    @Getter
    private Set<Sprite> spriteSet = new HashSet<Sprite>(); // maintain a local copy of the set of header sprites

    @Getter
    private  SpriteManager sman; // maintain a local copy of sprite manager

    public GraphViewer(ProxyPipe source) { super(source); setHQ();}

    public GraphViewer(GraphicGraph graph) { super(graph); setHQ();}

    public GraphViewer(Graph graph, ThreadingModel threadingModel) { super(graph, threadingModel); setHQ();}

    private void setHQ(){
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
    }


    @Synchronized
    public void initializeLabels(Graph graph){
        sman = new SpriteManager(graph);
        for (int i=0; i < 4; i++){
            Sprite header = sman.addSprite("header"+i);
            header.addAttribute("hid", "h"+i);
            switch (i){
                case 0:
                    header.addAttribute("ui.label", "Mouse LB: drag and move;" );
                    header.setPosition(Units.PX, 0, 15, 0);
                    break;
                case 1:
                    header.addAttribute("ui.label", "Mouse Wheel: zoom in/out;");
                    header.setPosition(Units.PX, 0, 30, 0);
                    break;
                case 2:
                    header.addAttribute("ui.label", "Mouse RB: resize graph;  ");
                    header.setPosition(Units.PX, 0, 45, 0);
                    break;
                case 3:
                    header.addAttribute("ui.label", "Double-click: show/remove all attributes");
                    header.setPosition(Units.PX, 0, 60, 0);
                    break;
            }
            spriteSet.add(header);
        }
        graph.getNodeSet().stream().forEach(n -> {
                    Sprite defaultAttr = sman.addSprite("schedInfo"+n.getId());
                    defaultAttr.addAttribute("ui.label",
                            "\tProc:"+n.getAttribute("processor")+" \n"+
                                    "\tSTime:"+n.getAttribute("startTime"));
                    defaultAttr.addAttribute("ui.style",
                            "text-alignment: center;\n" +
                                    "\ttext-background-mode: rounded-box;\n" +
                                    "\ttext-background-color: yellow;\n" +
                                    "\ttext-size: 16px;\n");
                    defaultAttr.attachToNode(n.getAttribute("id"));
                    defaultAttr.setPosition(Units.PX,44,180,90);
                    defaultAttr.setAttribute("ui.style", "text-mode: hidden;");
                    n.setAttribute("ui.label",
                            "ID:" + n.getAttribute("id") +
                                    " Wt:" + n.getAttribute("Weight"));
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
                n.removeAttribute("ui.class");
                String color;

                if (colorSet.keySet().contains(n.getAttribute("processor"))){
                    color = colorSet.get(n.getAttribute("processor"));
                    n.setAttribute("ui.style", color);
                    return;
                }
                
                color = "fill-color: rgb("+(int)(new Random().nextDouble()*255d)+","+
                        (int)(new Random().nextDouble()*255d)+","+
                        (int)(new Random().nextDouble()*255d)+"), " +
                        "rgb("+(int)((1d - new Random().nextDouble())*255d)+","+
                        (int)((1d - new Random().nextDouble())*255d)+","+
                        (int)((1d - new Random().nextDouble())*255d)+");";
                n.setAttribute("ui.style", color);

                colorSet.put(n.getAttribute("processor"), color);
            }
        });
    }

    @Synchronized
    public void updateNodes(){
        sman.forEach(s -> {
            if (!(spriteSet.contains(s))){
                s.setAttribute("ui.label",
                        "\tProc:"+s.getAttachment().getAttribute("processor")+" \n"+
                                "\tSTime:"+s.getAttachment().getAttribute("startTime"));
            }
        });
    }
}
