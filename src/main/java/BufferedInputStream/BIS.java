package BufferedInputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Will on 2017-07-27.
 *
 * Wrapper for BufferedInputStream for reading from System.in quickly.
 * LMK if it doesn't work was originally all static private methods I haven't tested this.
 */
public class BIS {

    private BufferedInputStream bis;

    public BIS(){
        bis = new BufferedInputStream(System.in);
    }

    /**
     * Reads Integers from a BufferedInputStream (until a space)
     */
    public int readInt() throws IOException {
        int value = 0;
        boolean negative = false;
        int c;

        while((c = bis.read()) != ' ') {
            if (c == '\n') break;
            if ((c >= '0' && c <= '9') || c == '-') {
                if (c == 45){
                    negative = true;
                    continue;
                }
                value = value * 10 + c - '0';
            }
        }
        return (negative)? value *-1 : value;
    }

    /**
     * Returns Doubles from a BufferedInputStream (until a space)
     */
    public double readDouble() throws IOException {
        double value = 0;
        boolean negative = false;
        boolean fp = false;
        int dp = 10;	// Number of decimal points = log(dp) (base 10) ?
        int c;

        while((c = bis.read()) != ' ') {
            if (c == '\n') break;
            if ((c >= '0' && c <= '9') || c == '-' || c == '.') {
                if (c == 45){
                    negative = true;
                    continue;
                } else if (c == '.'){
                    fp = true;
                    continue;
                }
                value = fp ? value + (double)(c - '0') / dp: value * 10 + c - '0';
                dp = fp ? dp*10: dp; // Update decimal pointer
            }
        }
        return (negative)? value *-1 : value;
    }

    /**
     * Reads a String from a BufferedInputStream (until \n)
     */
    public String readString() throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        int c;

        while ((c = bis.read()) != '\n' ){
            if (c == '\r'){continue;}//filter
            byteOut.write((byte)c);

        }
        return byteOut.toString();
    }
}
