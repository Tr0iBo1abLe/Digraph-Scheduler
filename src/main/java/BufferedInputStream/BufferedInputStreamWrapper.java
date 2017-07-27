package BufferedInputStream;

import java.io.*;

/**
 * Created by Will on 2017-07-27.
 *
 * Wrapper for BufferedInputStream for reading from System.in quickly.
 * LMK if it doesn't work was originally all static private methods I haven't tested this with files.
 */
public class BufferedInputStreamWrapper {

    private BufferedInputStream bis;

    /**
     * Creates a BufferedInputStreamWrapper for System.in.
     */
    public BufferedInputStreamWrapper(){
        bis = new BufferedInputStream((System.in));
    }

    /**
     * Creates a BufferedInputStreamWrapper for a given file.
     * @param file - file to read from.
     */
    public BufferedInputStreamWrapper(File file){
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
