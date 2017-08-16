package GUI.CSS;

public class GraphCSS {
    public static String css = "graph {\n" +
            "\tfill-color: white;\n" +
            "}\n" +
            "\n" +
            "node {\n" +
            "\tsize: 70px;\n" +
            "\tshape: circle;\n" +
            "\tfill-mode: gradient-diagonal2;\n" +
            "\tfill-color: orange, red;\n" +
            "\tstroke-mode: plain;\n" +
            "\tstroke-color: white;\n" +
            "\tshadow-mode: gradient-vertical;\n" +
            "\ttext-background-mode: rounded-box;\n" +
            "\ttext-background-color: yellow;\n" +
            "\ttext-alignment: center;\n" +
            "\ttext-size: 16px;\n" +
            "}\n" +
            "\n" +
            "\n" +
            "node:clicked {\n" +
            "\tfill-color: yellow;\n" +
            "\t}\n" +
            "\n" +
            "\n" +
            "node.sched {\n" +
            "\tfill-color: yellow, green;\n" +
            "\t}\n" +
            "\n" +
            "\n" +
            "edge {\n" +
            "\tsize: 10px;\n" +
            "\tfill-color: purple, blue;\n" +
            "\tfill-mode: gradient-diagonal2;\n" +
            "\ttext-background-mode: rounded-box;\n" +
            "\ttext-background-color: yellow;\n" +
            "\tshadow-mode: gradient-vertical;\n" +
            "\tarrow-shape: arrow;\n" +
            "\tarrow-size: 45px, 18px;\n" +
            "\ttext-size: 15px;\n" +
            "\t}\n" +
            "\n" +
            "\n" +
            "sprite {\n" +
            "\ttext-alignment: at-right;\n" +
            "\ttext-size: 15px;\n" +
            "\tfill-color: rgba(0,0,0,0);\n" +
            "\t}\n" +
            "\n";
}
