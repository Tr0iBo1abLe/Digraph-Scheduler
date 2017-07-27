package GraphHandler;

import lombok.NonNull;
import org.jgrapht.ext.ComponentNameProvider;

public class MyVertexLabelProvider implements ComponentNameProvider<MyVertex> {
    @Override
    public String getName(@NonNull final MyVertex myVertex) {
        return null;
    }
}
