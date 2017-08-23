package Parser;

import Graph.Edge;
import Graph.Exceptions.GraphException;
import Graph.Graph;
import Graph.Vertex;
import Parser.Exceptions.ParserException;
import Parser.Interfaces.IEdgeCtor;
import Parser.Interfaces.IVertexCtor;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InputParser<V extends Vertex, E extends Edge<V>> {

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
    private boolean inAttr = false;
    private LINE_STATE lineState = LINE_STATE.HEADER;
    private char nextSymbol;

    public InputParser(@NonNull IVertexCtor<V> vertexCtor,
                       @NonNull IEdgeCtor<V, E> edgeCtor) {
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

    /**
     * Wrapper for the parser; other objects can easily invoke Graph parsing on
     * a InputParser object when they provide a dot file.
     *
     * @param file the path of the file
     */
    public Graph<V, E> doParseAndFinaliseGraph(String file) {
        return doParseAndFinaliseGraph(new File(file));
    }

    /**
     * Wrapper for the parser; other objects can easily invoke Graph parsing on
     * a InputParser object when they provide a dot file.
     *
     * @param file the file
     */
    public Graph<V, E> doParseAndFinaliseGraph(File file) {
        Graph<V, E> graph = new Graph<V, E>();
        try {
            doParse(graph, new BufferedReader(new FileReader(file)));
        } catch (ParserException | FileNotFoundException e) {
            e.printStackTrace();
        }
        graph.finalise();
        return graph;
    }

    private void doParse(Graph<V, E> graph, @NonNull BufferedReader reader) throws ParserException {
        this.graph = graph;
        this.input = reader.lines().collect(Collectors.joining("\n"));
        int totalLength = input.length();
        while (pos < totalLength) {
            switch (currState) {
                case NEXT:
                case HEADER:
                    currState = processOne();
                    break;
                case NEXT_SYMBOL:
                    currState = processToNextSymbol();
                    break;
                case SEMICOLON:
                    try {
                        currState = processLine();
                    } catch (GraphException e) {
                        e.printStackTrace();
                    }
                    break;
                case LINEFEED:
                    currState = processLineFeed();
                    pos--;
                    break;
            }
            pos++;
        }
    }

    private STATE processToNextSymbol() {
        @NonNull char c = input.charAt(pos);
        if (c == nextSymbol) {
            tokenBuffer.add(strBuffer.toString());
            strBuffer.delete(0, strBuffer.length());
            return STATE.NEXT;
        } else {
            strBuffer.append(c);
            return STATE.NEXT_SYMBOL;
        }
    }

    private STATE processLineFeed() {
        return STATE.NEXT;
    }

    private STATE processLine() throws ParserException, GraphException {
        if (this.tokenBuffer.size() == 0) {
            flushBuffer();
            return STATE.NEXT;
        }
        Map<String, String> attrs = processAttrs();
        switch (lineState) {
            case EDGE:
                if (!attrs.containsKey("Weight")) {
                    // We skip this line
                    break;
                }
                if (this.tokenBuffer.size() != 2) {
                    throw new ParserException("Malformed Edge");
                }
                this.graph.addEdge(
                        this.edgeCtor.makeEdge(
                                this.graph.ensureVertex(this.tokenBuffer.get(0), this.vertexCtor),
                                this.graph.ensureVertex(this.tokenBuffer.get(1), this.vertexCtor),
                                attrs));
                break;
            case VERTEX:
                if (!attrs.containsKey("Weight")) {
                    // We skip this line
                    break;
                }
                if (this.tokenBuffer.size() != 1) {
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
        String[] attrTokens = last.split(",");
        for (String tk : attrTokens) {
            String[] association = tk.split("=");
            if (association.length != 2) {
                throw new ParserException("Malformed attribute");
            }
            attrMap.put(association[0], association[1]);
        }
        this.tokenBuffer.remove(tokenBuffer.size() - 1);
        return attrMap;
    }

    private STATE processFullHeader() throws ParserException {
        if (tokenBuffer.isEmpty()) throw new ParserException("Empty Header");
        if (tokenBuffer.size() < 1) throw new ParserException("Invalid Header");
        if (!tokenBuffer.get(0).matches("[Dd][Ii][Gg][Rr][Aa][Pp][Hh]")) throw new ParserException("Not a digraph");
        if (tokenBuffer.size() >= 2) {
            this.graph.setName(tokenBuffer.get(1));
        } else
            this.graph.setName("digraph");
        if (hasHeader) throw new ParserException("Malformed dot file");
        this.hasHeader = true;
        flushBuffer();
        return STATE.NEXT;
    }

    private STATE processOne() throws ParserException {
        @NonNull char c = input.charAt(pos);
        if (c == ' ' || c == '\t') {
            if (strBuffer.length() == 0) {
                return STATE.NEXT;
            } else {
                tokenBuffer.add(strBuffer.toString());
                strBuffer.delete(0, strBuffer.length());
                return STATE.NEXT;
            }
        } else if (c == '\"') {
            this.nextSymbol = '\"';
            return STATE.NEXT_SYMBOL;
        } else if (c == '[') {
            this.nextSymbol = ']';
            return STATE.NEXT_SYMBOL;
        } else if (c == '{') {
            processFullHeader();
            return STATE.HEADER;
        } else if (c == '}') {
            return STATE.BODY;
        } else if (c == '\n') {
            return STATE.LINEFEED;
        } else if (c == ';') {
            if (strBuffer.length() != 0) {
                tokenBuffer.add(strBuffer.toString());
                strBuffer.delete(0, strBuffer.length());
            }
            return STATE.SEMICOLON;
        } else if (c == '-') {
            char next = input.charAt(pos + 1);
            if (next == '>') {
                this.lineState = LINE_STATE.EDGE;
            } else {
                throw new ParserException("Not a digraph or invalid token");
            }
        } else if (c == '>') {
            return STATE.NEXT;
        } else if (c == '/') {
            char next = input.charAt(pos + 1);
            if (next == '/')
                this.lineState = LINE_STATE.COMMENT;
            return STATE.NEXT;
        } else {
            strBuffer.append(c);
            return STATE.NEXT;
        }

        return STATE.NEXT;
    }

    private enum STATE {HEADER, BODY, NEXT, LINEFEED, SEMICOLON, NEXT_SYMBOL}

    private enum LINE_STATE {COMMENT, VERTEX, EDGE, HEADER}

}

