package CommonInterface;

import Graph.Vertex;

/**
 * Created by e on 7/08/17.
 */
public interface ISearchState {
    int[] getProcessors();

    int[] getStartTimes();

    int getNumVertices();

    Vertex getLastVertex();

    int getUnderestimate();
}
