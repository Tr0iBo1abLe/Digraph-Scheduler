package Solver;

import java.util.stream.IntStream;

import org.graphstream.graph.Graph;

import lombok.Data;

/**
 * Created by mason on 31/07/17.
 */
@Data
public class DFSolver extends AbstractSolver {

    private static int bound = Integer.MAX_VALUE;
    private static SearchState result;

    public DFSolver(Graph graph, int processorCount) {
    	super(graph, processorCount);
    }
    
    @Override
    public void doSolve() {
		SearchState.init(graph);
		
		System.out.println(graph.getNodeCount());
		
    	SearchState s = new SearchState();

    	solving(s);  	

    	scheduleVertices(result);
    	
    }

    private void solving(SearchState s){
    	s.getLegalVertices().stream().forEach( v -> {
    		IntStream.of(0, processorCount-1).forEach( i -> {
    			SearchState next = new SearchState(s, v, i);
    			if (next.getDFcost() > bound){
    				return;
    			}
    			if (next.getSize() == graph.getNodeCount()){
    				updateLog(next, next.getDFcost());
    				return;
    			}
    			solving(next);
    		}
    		);
    	});
    }
    
    private void updateLog(SearchState s, int cost){
    	if (cost<bound){
    		bound = cost;
    		result = s;
    	}
    }

}
