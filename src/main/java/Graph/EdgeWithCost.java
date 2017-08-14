package Graph;

import Graph.Interfaces.IHasCost;
import lombok.Data;
import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by e on 28/07/17.
 */
@Data
public class EdgeWithCost<V extends Vertex> extends Edge<V> implements IHasCost {
    private final int cost;

    public EdgeWithCost(@NonNull final V from,
                        @NonNull final V to,
                        final int cost) {
        super(from, to);
        this.cost = cost;

    }

    @Override
    public String toString() {
        return super.toString().concat("Cost = " + String.valueOf(this.cost));
    }

    @Override
    public boolean equals(@NonNull final Object o) {
        return super.equals(o);
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attrs = new LinkedHashMap<>();
        attrs.put("Weight", String.valueOf(this.cost));
        return attrs;
    }
}
