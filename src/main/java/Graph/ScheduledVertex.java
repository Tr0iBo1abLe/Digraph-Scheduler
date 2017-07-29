package Graph;

import lombok.Data;
import lombok.NonNull;

import java.util.Map;

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

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attrs = super.getAttributes();
        attrs.put("Processor", String.valueOf(this.processor));
        attrs.put("Starttime", String.valueOf(this.startTime));
        return attrs;
    }
}
