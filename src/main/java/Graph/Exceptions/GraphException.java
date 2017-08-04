package Graph.Exceptions;

import lombok.NonNull;

public class GraphException extends Exception {
    public GraphException(@NonNull String s) {
        super(s);
    }
    public GraphException(){}

}
