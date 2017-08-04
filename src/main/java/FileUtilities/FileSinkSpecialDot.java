package FileUtilities;

import lombok.Data;
import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSinkDOT;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

public class FileSinkSpecialDot extends FileSinkDOT{
    private final String graphName;

    public FileSinkSpecialDot(String name) {
        super(true);
        this.graphName = name;
    }


    @Override
    protected void outputHeader() throws IOException {
        out = (PrintWriter) output;
        if(!this.digraph) throw new RuntimeException();
        out.printf("digraph %s{%n", this.graphName);
    }
}
