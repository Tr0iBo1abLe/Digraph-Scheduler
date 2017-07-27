package GraphHandler;

import org.jgrapht.ext.EdgeProvider;

import java.util.Map;

public class EProvider implements EdgeProvider<MyVertex, MyEdge>{

    @Override
    public MyEdge buildEdge(final MyVertex v1, final MyVertex v2, final String s, final Map<String, String> map) {
        return new MyEdge(v1, v2, s, Integer.parseInt(map.get("Weight")));
    }
}
