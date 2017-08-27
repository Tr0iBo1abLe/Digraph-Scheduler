import Solver.SearchState;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Test individual parts of the solvers.
 *
 * @author Will Molloy
 */
public class TestSolversWhiteBox {

    // this.processors = [ 1, 1, 2, 2, 3, 3 ]. other.processors = [ 5, 5, 8, 8, 6, 6 ]. Returns true, all duplicates map to the same value.
    @Test
    public void testSearchStateProcessorsAreShuffledTrue() {
        assertTrue(processorsAreShuffled(new int[]{1, 1, 2, 2, 3, 3}, new int[]{5, 5, 8, 8, 6, 6}));
    }

    // this.processors = [ 7, 7, 7, 4, 4, 5 ]. other.processors = [ 8, 8, 8, 5, 6, 7 ]. Returns false, one 4 maps to 5, the other to 6.
    @Test
    public void testSearchStateProcessorsAreShuffledFalseBadMapping() {
        assertFalse(processorsAreShuffled(new int[]{7, 7, 7, 4, 4, 5}, new int[]{5, 5, 8, 8, 6, 6}));
    }

    // this.processors = [ 7, 7, 7, 4, 4, 5 ]. other.processors = [ 8, 8, 8, 5, 5, 8 ]. Returns false, both 7 and 5 map to 8.
    @Test
    public void testSearchStateProcessorsAreShuffledFalseDuplicateMapping() {
        assertFalse(processorsAreShuffled(new int[]{7, 7, 7, 4, 4, 5}, new int[]{8, 8, 8, 5, 5, 8}));
    }


    /**
     * zzz. Copy code here, SearchState version cannot take two arguments due to hashCode() method.
     */
    private boolean processorsAreShuffled(final int[] A, final int[] B) {
        int[] processorMap = new int[9]; // to cache the mapped values
        Arrays.fill(processorMap, -1);

        for (int i = 0; i < A.length; i++) {
            if (A[i] < 0) continue; // check vertex has been assigned a processor
            if (processorMap[A[i]] < 0) { // check if value is mapped for this processor
                if (contains(processorMap, B[i])) return false;
                processorMap[A[i]] = B[i]; // initialise
            } else if (processorMap[A[i]] != B[i]) { // check
                return false; // short circuit
            }
        }
        return true;
    }

    private boolean containsDuplicate(final int[] processorMap) {
        boolean[] bitmap = new boolean[processorMap.length];
        for (int i : processorMap) {
            if (i > -1 && !(bitmap[i] ^= true))
                return true;
        }
        return false;
    }

    private boolean contains(final int[] array, final int value){
        for (int i : array){
            if (i == value) return true;
        }
        return false;
    }
}
