package Graph;

import lombok.Data;

@Data
public class Vertex implements IHasCost {
    protected final String Id;
    protected final int cost;
}
