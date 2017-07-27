import GraphHandler.EProvider;
import GraphHandler.MyEdge;
import GraphHandler.MyVertex;
import GraphHandler.VProvider;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.jgrapht.Graph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.ext.DOTImporter;
import org.jgrapht.ext.EdgeProvider;
import org.jgrapht.ext.ImportException;
import org.jgrapht.ext.VertexProvider;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.io.File;
import java.util.Map;

public class Main {

    private Main(){
        //Ensure this class is not instantiated
    }

    public static void main(String[] args) {
        if(args.length != 1) {
            System.err.println("$0 takes 1 argument");
            return;
        }


        File inputFile = new File(args[0]);
        if(!inputFile.exists() || !inputFile.canRead()) {
            System.err.println("Can't open file");
        }

        DirectedAcyclicGraph<MyVertex, MyEdge> g = new DirectedAcyclicGraph<MyVertex, MyEdge>(MyEdge.class);
        try {
            new DOTImporter<MyVertex, MyEdge>(new VProvider(), new EProvider()).importGraph(g, inputFile);
        } catch (ImportException e) {
            e.printStackTrace();
        }

        System.err.println(g.toString());

    }



}

