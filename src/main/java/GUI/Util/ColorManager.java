package GUI.Util;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A util class provides coloring supports for Gantt Chart and Visual Graph
 *
 * @author Mason Shi
 */
public class ColorManager {

    @Getter
    private static Map<String, String> colorSetForGraphNodes = new HashMap<String, String>(); //A mapping between rgb colors of graph nodes and processor ids
    @Getter
    private static Map<String, String> colorSetForGanttTasks = new HashMap<String, String>(); //A mapping between rgba colors of gantt tasks and processor ids
    @Getter
    private static Map<String, String> colorSetForGanttTasksBorder = new HashMap<String, String>(); //A mapping between rgba colors of gantt tasks and their border strokes

    private ColorManager() {
    } //This is a util class it should not be instantiated.

    /**
     * THIS METHOD MUST BE CALLED FIRST BEFORE ACCESING ANY OF THOSE COLOR MAPS.
     *
     * @param processorCount
     */
    public static void generateColor(int processorCount) {
        for (int i = 1; i <= processorCount; i++) {

            //prepare color for graph nodes
            String colorForNode;

            colorForNode = "rgb(" + (int) Math.sqrt((new Random().nextDouble() * 255d * new Random().nextDouble() * 255d)) + "," +
                    (int) Math.sqrt((new Random().nextDouble() * 255d * new Random().nextDouble() * 255d)) + "," +
                    (int) Math.sqrt((new Random().nextDouble() * 255d * new Random().nextDouble() * 255d)) + "), " +
                    "rgb(" + (int) Math.sqrt(((1d - new Random().nextDouble()) * 255d * (1d - new Random().nextDouble()) * 255d)) + "," +
                    (int) Math.sqrt(((1d - new Random().nextDouble()) * 255d * (1d - new Random().nextDouble()) * 255d)) + "," +
                    (int) Math.sqrt(((1d - new Random().nextDouble()) * 255d * (1d - new Random().nextDouble()) * 255d)) + ");";

            while (colorSetForGraphNodes.values().contains(colorForNode)) {
                colorForNode = "rgb(" + (int) Math.sqrt((new Random().nextDouble() * 255d * new Random().nextDouble() * 255d)) + "," +
                        (int) Math.sqrt((new Random().nextDouble() * 255d * new Random().nextDouble() * 255d)) + "," +
                        (int) Math.sqrt((new Random().nextDouble() * 255d * new Random().nextDouble() * 255d)) + "), " +
                        "rgb(" + (int) Math.sqrt(((1d - new Random().nextDouble()) * 255d * (1d - new Random().nextDouble()) * 255d)) + "," +
                        (int) Math.sqrt(((1d - new Random().nextDouble()) * 255d * (1d - new Random().nextDouble()) * 255d)) + "," +
                        (int) Math.sqrt(((1d - new Random().nextDouble()) * 255d * (1d - new Random().nextDouble()) * 255d)) + ");";
            }

            colorSetForGraphNodes.put(i + "", colorForNode);

            //prepare color for gantt tasks
            String colorForTask;

            colorForNode = colorForNode.substring(3);
            String[] helper = colorForNode.split(",");
            helper[3] = "rgba" + helper[3].substring(4);
            String refactoredColorFirstHalf = "";
            String refactoredColorSecondHalf = "";
            for (int j = 0; j < 3; j++) {
                refactoredColorFirstHalf += helper[j] + ",";
            }
            for (int j = 3; j < 6; j++) {
                refactoredColorSecondHalf += helper[j] + ",";
            }
            refactoredColorFirstHalf = refactoredColorFirstHalf.substring(0, refactoredColorFirstHalf.length() - 2);
            refactoredColorSecondHalf = refactoredColorSecondHalf.substring(0, refactoredColorSecondHalf.length() - 3);
            String refactoredColorForBorder = refactoredColorFirstHalf + ",1);";
            refactoredColorFirstHalf += ",0.5), ";
            refactoredColorSecondHalf += ",0.5)";
            colorForTask = "rgba" + refactoredColorFirstHalf + refactoredColorSecondHalf;

            //prepare color for gantt tasks border
            String colorForTaskBorder = "rgba" + refactoredColorForBorder;

            colorSetForGanttTasks.put(i + "", colorForTask);
            colorSetForGanttTasksBorder.put(i + "", colorForTaskBorder);
        }
    }
}
