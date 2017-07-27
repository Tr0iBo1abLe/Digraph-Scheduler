package GraphHandler;

import lombok.Data;

import java.util.Map;

@Data
public class MyEdge {
    private final MyVertex from;
    private final MyVertex to;
    private final String id;
    private final int cost;
}
