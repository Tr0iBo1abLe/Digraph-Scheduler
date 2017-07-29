package Exporter;

import Graph.Graph;
import Graph.Interfaces.ICollectibleAttribute;
import Graph.Vertex;
import Graph.Edge;
import lombok.NonNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GraphExporter<V extends Vertex, E extends Edge<V>> {

    public GraphExporter() {
    }

    public void doExport(@NonNull final Graph<V, E> graph,
                         @NonNull final Writer writer) {
        try {
            writer.write("digraph " + graph.getName() + " {\n");
            for(Object orderObj : graph.getOrder()) {
                if(orderObj instanceof Vertex) {
                    final Vertex v = (Vertex) orderObj;
                    writer.write(v.getId() + " ");
                    writeAttributes(v, writer);
                    writer.write("\n");
                }
                else if(orderObj instanceof Edge) {
                    final Edge<Vertex> e = (Edge<Vertex>) orderObj;
                    writer.write(e.getFrom().getId() + " -> " + e.getTo().getId() + " ");
                    writeAttributes(e, writer);
                    writer.write("\n");
                }
            }
            writer.write("}\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeAttributes(@NonNull final ICollectibleAttribute ica, @NonNull final Writer writer) throws IOException {
        final Set<Map.Entry<String, String>> entries = ica.getAttributes().entrySet();
        if(entries.isEmpty()) {
            return;
        }
        writer.write('[');
        final Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            writer.write(entry.getKey() + "=" + entry.getValue());
            if(iterator.hasNext()) {
                writer.write(",");
            }
        }
        writer.write(']');

    }
}
