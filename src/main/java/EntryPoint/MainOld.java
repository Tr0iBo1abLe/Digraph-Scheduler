package EntryPoint;

import Exporter.GraphExporter;
import Graph.EdgeWithCost;
import Graph.Graph;
import Graph.Vertex;
import Parser.EdgeCtor;
import Parser.InputParser;
import Parser.VertexCtor;
import SolverOld.*;
import SolverOld.Interfaces.ISolver;

import java.io.*;

public class MainOld {

    private MainOld(){
        //Ensure this class is not instantiated
    }

    public static void main(String[] args) {
        boolean par = false;
        int procN = 1;
        if(args.length < 1) {
            System.err.println("$0 takes 1 required argument(s)");
            return;
        }

        if(args.length >= 2) {
            procN = Integer.parseInt(args[1]);
        }

        if(args.length >= 3 && args[1].matches("[-]p")) {
            par = true;
        }

        File inputFile = new File(args[0]);
        if(!inputFile.exists() || !inputFile.canRead()) {
            System.err.println("Can't open file");
        }

        Graph<Vertex, EdgeWithCost<Vertex>> graph;

        InputParser<Vertex, EdgeWithCost<Vertex>> parser = new InputParser<Vertex, EdgeWithCost<Vertex>>(new VertexCtor(), new EdgeCtor());
        graph = parser.doParseAndFinaliseGraph(args[0]);
        System.out.print(graph.toString());

        ISolver solver;
        if(par) {
            solver = new AStarSolverPar(graph, procN);
        }
        else {
            solver = new AStarSolver(graph, procN);
        }
        solver.doSolve();


        final GraphExporter<Vertex,EdgeWithCost<Vertex>> vertexEdgeWithCostGraphExporter;
        vertexEdgeWithCostGraphExporter = new GraphExporter<Vertex, EdgeWithCost<Vertex>>();
        vertexEdgeWithCostGraphExporter.doExport(graph, new BufferedWriter(new OutputStreamWriter(System.out)));

    }



}

