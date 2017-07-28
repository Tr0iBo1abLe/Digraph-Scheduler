package Parser;

import Graph.Graph;
import Graph.SimpleEdge;
import Graph.Vertex;
import Parser.Exceptions.ParserException;
import fj.data.LazyString;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by edward on 7/27/17.
 */
public class InputParser<V extends Vertex, E extends SimpleEdge<V>> {

    private enum STATE {HEADER, BODY, VERTEX, EDGE, ATTRIBUTE, NEXT};
    private IVertexCtor<V> vertexCtor;
    private IEdgeCtor<V, E> edgeCtor;

    /* Parser Buffer and states */
    private List<StringBuffer> sBuf;
    private STATE currState = STATE.HEADER;
    private int pos = 0;
    private String input;
    private Map<String, String> attrBuf;
    private Graph<V, E> graph;

    public InputParser(@NonNull IVertexCtor<V> vertexCtor,
                       @NonNull IEdgeCtor<V, E> edgeCtor)
    {
        this.vertexCtor = vertexCtor;
        this.edgeCtor = edgeCtor;
        this.sBuf = new LinkedList<>();
    }

    public void doParse(Graph<V, E> graph, @NonNull BufferedReader reader) throws ParserException {
        this.graph = graph;
        input = reader.lines().collect(Collectors.joining());
        switch(currState) {
            case HEADER:
                currState = processHeader();
        }
    }

    private STATE processHeader() throws ParserException {
        @NonNull char c = input.charAt(pos);
        if (c == '{') {
            processFullHeader();
        }
    }

    private void processFullHeader() throws ParserException {
        if(sBuf.isEmpty()) throw new ParserException("Empty Header");
    }



    public STATE processOne(){
        @NonNull char c = input.charAt(pos);
        if(c == ' ')
    }

    public STATE processOneToken() {

    }

}

