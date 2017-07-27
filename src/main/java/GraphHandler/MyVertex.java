package GraphHandler;

import lombok.Data;

@Data
public class MyVertex {
    private final String id;
    private final int vertexCost;
    private int processor;
    private int startTime;
}
