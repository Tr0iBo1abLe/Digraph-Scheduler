package Graph;

import lombok.Data;

@Data
public class Vertex implements IHasCost {
    private final String Id;
    private final int cost;
}
