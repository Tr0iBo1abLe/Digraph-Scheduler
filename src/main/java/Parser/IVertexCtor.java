package Parser;

import java.util.Map;

/**
 * Created by e on 28/07/17.
 */
public interface IVertexCtor<V> {
    V makeVertex(String id, Map<String, String> attrs);
}
