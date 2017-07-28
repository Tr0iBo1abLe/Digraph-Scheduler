package Graph;

import lombok.Data;

@Data
public class ScheduledVertex extends Vertex{
    private final int processor;
    private final int startTime;
}
