package CommonInterface;

import GUI.Interfaces.IUpdatableState;

import java.util.Timer;

/**
 * Created by e on 30/07/17.
 */
public interface ISolver {
    void doSolve();

    void associateUI(IUpdatableState ui);

    int getProcessorCount();

    int getFinalTime();

}
