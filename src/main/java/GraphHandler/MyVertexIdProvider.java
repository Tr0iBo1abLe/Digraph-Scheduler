package GraphHandler;

import lombok.NonNull;
import org.jgrapht.ext.ComponentNameProvider;

/**
 * Created by e on 27/07/17.
 */
public class MyVertexIdProvider implements ComponentNameProvider<MyVertex> {
    @Override
    @NonNull
    public String getName(@NonNull final MyVertex myVertex) {
        return myVertex.getId();
    }
}
