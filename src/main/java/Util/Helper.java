package Util;

import lombok.NonNull;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Created by e on 2/08/17.
 */
public class Helper {
    public static void finalise(Graph g) {
        g.getNodeSet().forEach(v -> calculateBottomLevels(v, 0));
    }

    public static void calculateBottomLevels(@NonNull final Node v,
                                             final double level) {
        Double res;
        res = v.getAttribute("BL", Double.class);
        if(res == null) {
            v.setAttribute("BL", 0d);
            res = 0d;
        }
        if(res < level) {
            v.setAttribute("BL", level);
        }
        else {
            v.getEnteringEdgeSet().forEach(e -> {
                Node n = e.getSourceNode();
                calculateBottomLevels(n, level + v.getAttribute("Weight", Double.class));
            });
        }
    }

    public static void stripUneeded(@NonNull final Graph g) {
        g.getNodeSet().forEach(e -> {
            e.removeAttribute("BL");
        });
    }
}
