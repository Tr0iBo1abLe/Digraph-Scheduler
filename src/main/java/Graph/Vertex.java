package Graph;

import lombok.Data;

@Data
public class Vertex implements IHasCost {
    private final int Id;
    private final int cost;
}
