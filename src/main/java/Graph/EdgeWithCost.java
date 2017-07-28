package Graph;

import lombok.Data;

/**
 * Created by e on 28/07/17.
 */
@Data
public class EdgeWithCost<V> extends Graph.SimpleEdge<V> implements IHasCost {
    private final int cost;
}
