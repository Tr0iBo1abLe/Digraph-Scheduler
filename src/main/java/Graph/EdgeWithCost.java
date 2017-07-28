package Graph;

import lombok.Data;
import lombok.NonNull;

/**
 * Created by e on 28/07/17.
 */
@Data
public class EdgeWithCost<V> extends SimpleEdge<V> implements IHasCost {
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
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
