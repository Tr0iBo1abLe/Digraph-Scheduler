package GUI.Interfaces;

import CommonInterface.ISearchState;
import Solver.AbstractSolver;

/**
 * Created by e on 8/08/17.
 * @author Edward Huang
 */
public interface IUpdatableState {
    void updateWithState(ISearchState searchState, AbstractSolver Solver);
}
