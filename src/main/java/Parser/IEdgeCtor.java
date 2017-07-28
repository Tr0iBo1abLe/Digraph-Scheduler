package Parser;

import Graph.IEdge;
import lombok.NonNull;

import java.util.Map;

/**
 * Created by e on 28/07/17.
 */
public interface IEdgeCtor<V, E extends IEdge> {
    public E makeEdge(@NonNull final V vFrom,
                      @NonNull final V vTo,
                      final Map<String, String> attrs);
}
