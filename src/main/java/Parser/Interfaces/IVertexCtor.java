package Parser.Interfaces;

import Graph.Exceptions.GraphException;
import lombok.NonNull;

import java.util.Map;

/**
 * Created by e on 28/07/17.
 */
public interface IVertexCtor<V> {
    V makeVertex(@NonNull final String id, final Map<String, String> attrs) throws GraphException;

    V makeVertex(@NonNull final String id);
}
