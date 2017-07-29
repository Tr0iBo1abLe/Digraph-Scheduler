package GraphHandler;

import lombok.Data;

@Data
public class MyVertex {
    private final String id;
    private final int vertexCost;
    private int processor;
    private int startTime;


    public MyVertex(String id, int vertexCost, int processor, int startTime) {
        this.id = id;
        this.vertexCost = vertexCost;
        this.processor = processor;
        this.startTime = startTime;
    }
}
