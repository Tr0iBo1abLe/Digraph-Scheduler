package GUI;

import org.graphstream.graph.Graph;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;

import java.awt.event.*;

public class GraphViewer extends Viewer implements ViewerListener, MouseWheelListener{

    public GraphViewer(ProxyPipe source) {
        super(source);
    }

    public GraphViewer(GraphicGraph graph) {
        super(graph);
    }

    public GraphViewer(Graph graph, ThreadingModel threadingModel) {
        super(graph, threadingModel);
    }


    @Override
    public void viewClosed(String s) {

    }

    @Override
    public void buttonPushed(String s) {

    }

    @Override
    public void buttonReleased(String s) {
        //don't care.
    }

    /**
     * Invoked when the mouse wheel is rotated.
     *
     * @param e
     * @see MouseWheelEvent
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
