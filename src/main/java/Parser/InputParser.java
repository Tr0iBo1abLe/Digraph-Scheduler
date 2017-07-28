package Parser;

import Graph.Graph;
import Graph.SimpleEdge;
import Graph.Vertex;
import Parser.Exceptions.ParserException;
import lombok.NonNull;

import java.io.BufferedReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InputParser<V extends Vertex, E extends SimpleEdge<V>> {

    private enum STATE {HEADER, BODY, NEXT, LINEFEED, SEMICOLON}
    private enum LINE_STATE { COMMENT, VERTEX, EDGE, HEADER }
    private IVertexCtor<V> vertexCtor;
    private IEdgeCtor<V, E> edgeCtor;

    /* Parser Buffer and states */
    private List<String> tokenBuffer;
    private StringBuffer strBuffer;
    private STATE currState = STATE.NEXT;
    private int pos = 0;
    private String input;
    private Graph<V, E> graph;
    private boolean hasHeader = false;
    private char nextTokenToFind;
    private LINE_STATE lineState = LINE_STATE.HEADER;

    public InputParser(@NonNull IVertexCtor<V> vertexCtor,
                       @NonNull IEdgeCtor<V, E> edgeCtor)
    {
        this.vertexCtor = vertexCtor;
        this.edgeCtor = edgeCtor;
        this.tokenBuffer = new LinkedList<>();
        this.strBuffer = new StringBuffer();
    }

    private void flushBuffer() {
        this.strBuffer.delete(0, strBuffer.length());
        this.tokenBuffer.clear();
        this.lineState = LINE_STATE.VERTEX;
    }

    public void doParse(Graph<V, E> graph, @NonNull BufferedReader reader) throws ParserException {
        this.graph = graph;
        this.input = reader.lines().collect(Collectors.joining("\n"));
        int totalLength = input.length();
        while(pos < totalLength) {
            switch(currState) {
                case NEXT:
                case HEADER:
                    currState = processOne();
                    break;
                case SEMICOLON:
                    currState = processLine();
                    break;
                case LINEFEED:
                    currState = STATE.NEXT;
                    break;
            }
            pos++;
        }
    }

    private STATE processTilLine() {
        if(this.input.charAt(pos) != '\n')
            return STATE.NEXT;
        else
            return STATE.LINEFEED;
    }

    private STATE processLine() throws ParserException {
        if(this.tokenBuffer.size() == 0) {
            flushBuffer();
            return STATE.NEXT;
        }
        Map<String, String> attrs = processAttrs();
        switch (lineState) {
            case EDGE:
                if(this.tokenBuffer.size() != 2) {
                    throw new ParserException("Malformed Edge");
                }
                this.graph.addEdge(
                        this.edgeCtor.makeEdge(
                                this.graph.ensureVertex(this.tokenBuffer.get(0), this.vertexCtor),
                                this.graph.ensureVertex(this.tokenBuffer.get(1), this.vertexCtor),
                                attrs));
                break;
            case VERTEX:
                if(this.tokenBuffer.size() != 1) {
                    throw new ParserException("Malformed Vertex");
                }
                this.graph.addVertex(
                        this.vertexCtor.makeVertex(
                                this.tokenBuffer.get(0),
                                attrs
                        )
                );
                break;
            case COMMENT:
            default:
                break;
        }
        flushBuffer();
        return STATE.NEXT;
    }

    private Map<String, String> processAttrs() throws ParserException {
        String last = tokenBuffer.get(tokenBuffer.size() - 1);
        Map<String, String> attrMap = new LinkedHashMap<>();
        if (last.startsWith("[") && last.endsWith("]")) {
            String[] attrTokens = last.substring(1, last.length() - 1 ).split(",");
            for(String tk : attrTokens) {
                String[] association = tk.split("=");
                if(association.length != 2) {
                    throw new ParserException("Malformed attribute");
                }
                attrMap.put(association[0], association[1]);
            }
            this.tokenBuffer.remove(tokenBuffer.size()-1);
        }
        return attrMap;
    }


    private STATE processFullHeader() throws ParserException {
        if(tokenBuffer.isEmpty()) throw new ParserException("Empty Header");
        if(hasHeader) throw new ParserException("Malformed dot file");
        this.hasHeader = true;
        flushBuffer();
        return STATE.NEXT;
    }

    public STATE processOne() throws ParserException {
        @NonNull char c = input.charAt(pos);
        if(c == ' ') {
            if(strBuffer.length() == 0) {
                return STATE.NEXT;
            }
            else {
                tokenBuffer.add(strBuffer.toString());
                strBuffer.delete(0, strBuffer.length());
                return STATE.NEXT;
            }
        }
        else if (c == '{') {
            processFullHeader();
            return STATE.HEADER;
        }
        else if (c == '}') {
            return STATE.BODY;
        }
        else if (c == '\n') {
            return STATE.LINEFEED;
        }
        else if (c == ';') {
            tokenBuffer.add(strBuffer.toString());
            strBuffer.delete(0, strBuffer.length());
            return STATE.SEMICOLON;
        }
        else if(c == '-') {
            char next = input.charAt(pos+1);
            if(next == '>') {
                this.lineState = LINE_STATE.EDGE;
            }
            else {
                throw new ParserException("Not a digraph or invalid token");
            }
        }
        else if(c == '>') {
            return STATE.NEXT;
        }
        else if(c == '/') {
            char next = input.charAt(pos+1);
            if(next == '/')
                this.lineState = LINE_STATE.COMMENT;
            return STATE.NEXT;
        }
        else {
            strBuffer.append(c);
            return STATE.NEXT;
        }

        return STATE.NEXT;
    }

}

