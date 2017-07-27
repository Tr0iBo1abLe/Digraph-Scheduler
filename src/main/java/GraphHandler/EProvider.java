package GraphHandler;

import org.jgrapht.ext.EdgeProvider;

import java.util.Map;

/**
 * Created by e on 27/07/17.
 */
public class EProvider implements EdgeProvider<MyVertex, MyEdge>{

    @Override
    public MyEdge buildEdge(MyVertex v1, MyVertex v2, String s, Map<String, String> map) {
        return new MyEdge(v1, v2, s, Integer.parseInt(map.get("Weight")));
    }
}
