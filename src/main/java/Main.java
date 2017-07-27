import GraphHandler.*;
import org.jgrapht.Graph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.DOTImporter;
import org.jgrapht.ext.ImportException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Set;

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

        Graph<MyVertex, MyEdge> g = new DirectedAcyclicGraph<MyVertex, MyEdge>(MyEdge.class);
        try {
            DOTImporter<MyVertex, MyEdge> im = new DOTImporter<MyVertex, MyEdge>(new VProvider(), new EProvider());
            im.importGraph(g, inputFile);
        } catch (ImportException e) {
            e.printStackTrace();
        }

        Set<MyVertex> set = g.vertexSet();
        for(MyVertex v : set) {
            v.setProcessor(1);
            v.setStartTime(1);
            System.err.println(v.toString());
        }

        Set<MyEdge> eset = g.edgeSet();
        for(MyEdge v : eset) {
            System.err.println(v.toString());
        }


        Writer w = new BufferedWriter(new OutputStreamWriter(System.out));

        new DOTExporter<MyVertex, MyEdge>(
                new MyVertexIdProvider(),
                null,
                null,
                new MyVertexAttributeProvider(),
                null
        ).exportGraph(g, w);


    }



}

