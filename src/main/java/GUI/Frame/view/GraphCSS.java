package GUI.Frame.view;

import lombok.experimental.UtilityClass;

/**
 * Created by e on 28/08/17.
 */
@UtilityClass
public class GraphCSS {

    public final static String GRAPH_CSS = " graph { fill-color: white; } node { size: 70px; shape: circle; fill-mode: gradient-diagonal2; fill-color: orange, red; stroke-mode: plain; stroke-color: white; shadow-mode: gradient-vertical; text-background-mode: rounded-box; text-background-color: yellow; text-alignment: center; text-size: 16px; } node:clicked { fill-color: yellow; } node.sched { fill-color: yellow, green; } edge { size: 10px; fill-color: purple, blue; fill-mode: gradient-diagonal2; text-background-mode: rounded-box; text-background-color: yellow; shadow-mode: gradient-vertical; arrow-shape: arrow; arrow-size: 45px, 18px; text-size: 15px; } sprite { text-alignment: at-right; text-size: 15px; fill-color: rgba(0,0,0,0); } ";

}
