package Parser;

import Graph.Graph;
import Graph.SimpleEdge;
import Graph.Vertex;
import fj.data.LazyString;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by edward on 7/27/17.
 */
public class InputParser<V extends Vertex, E extends SimpleEdge<V>> {

    private enum STATE {HEADER, BODY, VERTEX, EDGE, ATTRIBUTE};
    private IVertexCtor<V> vertexCtor;
    private IEdgeCtor<V, E> edgeCtor;

    /* Parser Buffer and states */
    private StringBuffer sBuf;
    private STATE currState = STATE.HEADER;
    private int pos = 0;

    public InputParser(@NonNull IVertexCtor<V> vertexCtor,
                       @NonNull IEdgeCtor<V, E> edgeCtor) {
        this.vertexCtor = vertexCtor;
        this.edgeCtor = edgeCtor;
    }

    public void doParse(Graph<V, E> graph, @NonNull BufferedReader reader) {
        String line;
        try {
            while((line = reader.readLine()) != null) {
                switch(currState) {
                    case HEADER:

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public STATE processOne(){
        return null;
    }

}

