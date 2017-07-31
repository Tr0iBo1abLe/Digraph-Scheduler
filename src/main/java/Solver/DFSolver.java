package Solver;

import Graph.EdgeWithCost;
import Graph.Exceptions.GraphException;
import Graph.Graph;
import Graph.Vertex;
import Solver.Interfaces.ISolver;
import lombok.Data;

import java.util.*;
import java.util.stream.IntStream;


/**
 * Created by mason on 31/07/17.
 */
@Data
public class DFSolver implements ISolver {

	private final Graph<Vertex, EdgeWithCost<Vertex>> graph;
    private final int processorCount;
    
    private static int log = Integer.MAX_VALUE;
    private static SearchState result;

    @Override
    public void doSolve() {

    	SearchState s = new SearchState(graph);

    	solving(s);  	

    	scheduleVertices(result);
    	
    }

    private void solving(SearchState s){
    	s.getLegalVertices().stream().forEach( v -> {
    		IntStream.of(0, processorCount-1).forEach( i -> {
    			SearchState next = new SearchState(s, v, i);
    			if (next.getDFcost() >= log){
    				return;
    			}
    			if (next.getSize() == graph.getVertices().size()){
    				updateLog(next, next.getDFcost());
    				return;
    			}
    			solving(next);
    		}
    		);
    	});
    }
    
    private void updateLog(SearchState s, int cost){
    	if (cost<log){
    		log = cost;
    		result = s;
    	}
    }

    private void scheduleVertices(SearchState s) {
    	final int[] processors = Arrays.stream(s.getProcessors()).map(x -> x+1).toArray();
    	final int[] startTimes = s.getStartTimes();
    	for (int i = 0; i < graph.getVertices().size(); i++) {
            Vertex v = graph.lookUpVertexById(i);
            try {
                graph.scheduleVertex(v,processors[i], startTimes[i]);
            } catch (GraphException e) {
                e.printStackTrace();
            }
        }
    }
}
