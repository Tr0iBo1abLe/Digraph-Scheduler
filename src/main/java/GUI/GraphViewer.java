package GUI;

import GUI.Util.ColorManager;
import lombok.Getter;
import lombok.Synchronized;
import org.graphstream.graph.Graph;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants.Units;

import java.util.*;

/**
 * This is one of the event handler for GS graph.
 * It is managed by Controller (see #Controller), and responsible for:
 * - initializing the Viewer that generates the GUI component (ViewPanel)
 * - customizing the default behavior by extending the Viewer class from GS framework
 * - updating the states of nodes, edges, and sprites whenever requested by Controller
 * - providing customized coloring using ColorManager (#see ColorManager)
 * @author Mason Shi
 */
public class GraphViewer extends Viewer{

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
                String color = ColorManager.getColorSetForGraphNodes().get(n.getAttribute("processor")+"");
                n.setAttribute("ui.style", "fill-color: " + color);
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
