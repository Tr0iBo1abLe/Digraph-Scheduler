package CommonInterface;

import GUI.IUpdatableState;

/**
 * Created by e on 30/07/17.
 */
public interface ISolver {
    void doSolve();

    void associateUI(IUpdatableState ui);

    int getProcessorCount();

    int getFinalTime();

}
