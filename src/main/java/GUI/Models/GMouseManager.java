package GUI.Models;


import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;


import java.awt.event.*;
import java.util.Set;
import java.util.stream.Collectors;

public class GMouseManager implements MouseMotionListener, MouseListener, MouseWheelListener{

    private View view;
    private Graph graph;

    private int previousX;
    private int previousY;

    private boolean checkpoint = false;

    public GMouseManager(View v, Graph g) {
        this.view = v;
        this.graph = g;
        view.addMouseMotionListener(this);
        view.addMouseListener(this);
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3){
            view.getCamera().resetView();
        }else{
            if (e.getClickCount()==2){
                if (!checkpoint){
                    graph.getNodeSet().stream().forEach(n -> n.addAttribute("ui.label",
                            "ID:"+n.getId()+" \n"+
                                    "\tWt:"+n.getAttribute("Weight")+" \n"+
                                    "\tProc:"+n.getAttribute("Processor")+" \n"+
                                    "\tSTime:"+n.getAttribute("ST"))
                    );
                    graph.getEdgeSet().stream().forEach(eg -> eg.addAttribute("ui.label",
                            "Wt:"+eg.getAttribute("Weight"))
                    );
                }else {
                    graph.getNodeSet().stream().forEach(n -> n.removeAttribute("ui.label"));
                    graph.getEdgeSet().stream().forEach(eg -> eg.removeAttribute("ui.label"));
                }
                checkpoint = !checkpoint;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
//        super.mousePressed(event);
        previousX = event.getX();
        previousY = event.getY();
//        System.out.println(previousX + " " + previousY);
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent event) {
//        super.mouseDragged(event);
//        System.out.println(event.getSource().getClass().toString());
        GraphicElement ge = view.findNodeOrSpriteAt((double)event.getX(), (double)event.getY());
        if (ge == null){
            int y = event.getY();
            int x = event.getX();
//            System.out.println(x + " " + y);
//            System.out.println(((double)x - (double)previousX)+" "+((double)y - (double)previousY));
            Point3 p = view.getCamera().getViewCenter();
//            System.out.println(p.x + " " + p.y);
            Point3 px = view.getCamera().transformGuToPx(p.x, p.y, p.z);
            Point3 pgu = view.getCamera().transformPxToGu(px.x + ((double)x - (double)previousX)*(-1.00), px.y + ((double)y - (double)previousY)*(-1.00));
            view.getCamera().setViewCenter(pgu.x, pgu.y, pgu.z);
//        view.getCamera().setViewCenter((double)x,(double)y,p.z);
            previousX = x;
            previousY = y;
        }
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {

    }

    /**
     * Invoked when the mouse wheel is rotated.
     *
     * @param e
     * @see MouseWheelEvent
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        if (e.getWheelRotation()<0){
            view.getCamera().setViewPercent(view.getCamera().getViewPercent()*0.8);
        }else if ((e.getWheelRotation()>0)){
            view.getCamera().setViewPercent(view.getCamera().getViewPercent()*1.5);
        }

    }
}
