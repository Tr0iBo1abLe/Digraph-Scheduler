package GUI.Models;

import org.graphstream.graph.Node;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.util.DefaultMouseManager;
import org.graphstream.ui.view.util.MouseManager;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GMouseManager implements MouseMotionListener, MouseListener{

    private View view;

    private int previousX;
    private int previousY;

    public GMouseManager(View v) {
        super();
        this.view = v;
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

    }

    @Override
    public void mousePressed(MouseEvent event) {
//        super.mousePressed(event);
        previousX = event.getX();
        previousY = event.getY();
        System.out.println(previousX + " " + previousY);
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

}
