package Graph.Exceptions;

import lombok.NonNull;

public class UncheckedException extends RuntimeException {
    public UncheckedException(@NonNull String s) {
        super(s);
    }

    public UncheckedException() {
    }
}
