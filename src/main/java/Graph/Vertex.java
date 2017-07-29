package Graph;

import Graph.Interfaces.ICollectibleAttribute;
import Graph.Interfaces.IHasCost;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Vertex implements IHasCost, ICollectibleAttribute {
    protected final String id;
    protected final int cost;

    @Override
    public boolean equals(Object o) {
        Vertex v = (Vertex) o;
        return id.equals(v.getId());
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attrs = new LinkedHashMap<>();
        attrs.put("Weight", String.valueOf(this.cost));
        return attrs;
    }
}
