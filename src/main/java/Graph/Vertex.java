package Graph;

import Graph.Interfaces.ICollectibleAttribute;
import Graph.Interfaces.IHasCost;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(exclude = {"cost", "bottomLevel", "processor", "startTime", "assignedId"})
@ToString
public class Vertex implements IHasCost, ICollectibleAttribute {
    protected final String id;
    protected final int cost;
    protected int bottomLevel;
    protected int assignedId;
    protected int processor = -1;
    protected int startTime = -1;

    @Override
    @NonNull
    public Map<String, String> getAttributes() {
        Map<String, String> attrs = new LinkedHashMap<>();
        attrs.put("Weight", String.valueOf(this.cost));
        if(this.processor != -1)
            attrs.put("Processor", String.valueOf(this.processor));
        if(this.startTime != -1)
            attrs.put("Start", String.valueOf(this.startTime));
        return attrs;
    }
}
