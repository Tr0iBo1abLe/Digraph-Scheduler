package GraphHandler;

import org.jgrapht.ext.VertexProvider;

import java.util.Map;

/**
 * Created by e on 27/07/17.
 */
public class VProvider implements VertexProvider<MyVertex>{
    @Override
    public MyVertex buildVertex(final String s, final Map<String, String> map) {
        return new MyVertex(s, Integer.parseInt(map.get("Weight")));
    }
}
