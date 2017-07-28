package Graph;

import lombok.Data;

@Data
public class Vertex implements IHasCost {
    protected final String id;
    protected final int cost;

    @Override
    public boolean equals(Object o) {
        Vertex v = (Vertex) o;
        return id.equals(v.getId());
    }
}
