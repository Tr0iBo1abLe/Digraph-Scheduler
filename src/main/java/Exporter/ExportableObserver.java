package Exporter;

import Graph.Vertex;
import Graph.*;
import lombok.Data;
import lombok.Value;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by you on 8/23/2017.
 */
@Value
public class ExportableObserver implements Observer {
    private OutputStream os;

    @Override
    @SuppressWarnings("unchecked")
    public void update(Observable o, Object arg) {
        if(o instanceof Graph) {
            final GraphExporter<Vertex, EdgeWithCost<Vertex>> vertexEdgeWithCostGraphExporter;
            vertexEdgeWithCostGraphExporter = new GraphExporter<Vertex, EdgeWithCost<Vertex>>();
            vertexEdgeWithCostGraphExporter.doExport((Graph<Vertex, EdgeWithCost<Vertex>>) o, new BufferedWriter(new OutputStreamWriter(os)));
        }
    }
}
