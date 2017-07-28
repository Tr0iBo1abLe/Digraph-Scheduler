package Graph;

import lombok.Data;
import lombok.NonNull;

@Data
public class ScheduledVertex extends Vertex{
    private final int processor;
    private final int startTime;

    public ScheduledVertex(@NonNull final String id,
                           final int cost,
                           final int processor,
                           final int startTime) {
        super(id, cost);
        this.processor = processor;
        this.startTime = startTime;
    }
}
