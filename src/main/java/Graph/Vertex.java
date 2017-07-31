package Graph;

import Graph.Interfaces.ICollectibleAttribute;
import Graph.Interfaces.IHasCost;
import lombok.Data;
import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Vertex implements IHasCost, ICollectibleAttribute {
    protected final String id;
    protected final int cost;
    protected int bottomLevel;
    protected int assignedId;
    protected int processor = -1;
    protected int startTime = -1;

    @Override
    public boolean equals(@NonNull final Object o) {
        if(o instanceof Vertex) {
            Vertex v = (Vertex) o;
            return id.equals(v.getId());
        }
        else return false;
    }

    @Override
    @NonNull
    public Map<String, String> getAttributes() {
        Map<String, String> attrs = new LinkedHashMap<>();
        attrs.put("Weight", String.valueOf(this.cost));
        if(this.processor != -1)
            attrs.put("Processor", String.valueOf(this.processor));
        if(this.startTime != -1)
            attrs.put("Starttime", String.valueOf(this.startTime));
        return attrs;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
