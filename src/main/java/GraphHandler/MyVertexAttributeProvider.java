package GraphHandler;

import org.jgrapht.ext.ComponentAttributeProvider;
import org.jgrapht.ext.ComponentNameProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by e on 27/07/17.
 */
public class MyVertexAttributeProvider implements ComponentAttributeProvider<MyVertex> {
    @Override
    public Map<String, String> getComponentAttributes(MyVertex v) {
        Map<String, String> map = new HashMap<>();
        map.put("Weight", String.valueOf(v.getVertexCost()));
        map.put("Start", String.valueOf(v.getStartTime()));
        map.put("Processor", String.valueOf(v.getProcessor()));
        return map;
    }
}
