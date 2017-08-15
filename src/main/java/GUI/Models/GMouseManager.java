package GUI.Models;


import GUI.GraphViewer;
import lombok.Synchronized;
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

    public GMouseManager(View v, Graph graph) {
        this.view = v;
        this.graph = graph;
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
    @Synchronized
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3){
            view.getCamera().resetView();
        }else{
            if (e.getClickCount()==2){
                if (!checkpoint){
                    enableAttrDisplay();
                }else {
                    disableAttrDisplay();
                }
                checkpoint = !checkpoint;
            }
        }
    }

    @Synchronized
    private void enableAttrDisplay(){
        graph.getNodeSet().stream().forEach(n -> n.setAttribute("ui.style", "text-mode: normal;"));
        graph.getEdgeSet().stream().forEach(eg -> eg.setAttribute("ui.style", "text-mode: normal;"));
    }


    @Synchronized
    private void disableAttrDisplay(){
        graph.getNodeSet().stream().forEach(n -> n.setAttribute("ui.style", "text-mode: hidden;"));
        graph.getEdgeSet().stream().forEach(eg -> eg.setAttribute("ui.style", "text-mode: hidden;"));
    }


    @Override
    @Synchronized
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
    @Synchronized
    public void mouseDragged(MouseEvent event) {
        GraphicElement ge = view.findNodeOrSpriteAt((double)event.getX(), (double)event.getY());
        if (ge == null){
            int y = event.getY();
            int x = event.getX();
            Point3 p = view.getCamera().getViewCenter();
            Point3 px = view.getCamera().transformGuToPx(p.x, p.y, p.z);
            Point3 pgu = view.getCamera().transformPxToGu(px.x + ((double)x - (double)previousX)*(-1.00), px.y + ((double)y - (double)previousY)*(-1.00));
            view.getCamera().setViewCenter(pgu.x, pgu.y, pgu.z);
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
    @Synchronized
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation()<0){
            view.getCamera().setViewPercent(view.getCamera().getViewPercent()*0.8);
        }else if ((e.getWheelRotation()>0)){
            view.getCamera().setViewPercent(view.getCamera().getViewPercent()*1.5);
        }
    }
}
