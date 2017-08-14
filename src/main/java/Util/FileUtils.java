package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utility class that performs tasks on Files that may be useful.
 * <p>
 * Created by Will on 2017-08-02.
 */
public class FileUtils {

    public static String readFileToString(String fileName) {
        StringBuilder output = new StringBuilder();
        try {
            String input;
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            while ((input = bufferedReader.readLine()) != null) {
                output.append(input);
                output.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }
}
