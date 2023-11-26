package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing A = new AListNoResizing<Integer>();
        A.addLast(4);
        A.addLast(5);
        A.addLast(6);

        BuggyAList B = new BuggyAList<Integer>();
        B.addLast(4);
        B.addLast(5);
        B.addLast(6);

        for (int i = 0; i < 3; i++) {
          assertEquals(A.removeLast(), B.removeLast());
        }
    }
    @Test
    public void randomizedTest() {
      AListNoResizing<Integer> L = new AListNoResizing<>();
      BuggyAList<Integer> K = new BuggyAList<>();

      int N = 5000;
      for (int i = 0; i < N; i += 1) {
        int operationNumber = StdRandom.uniform(0, 4);
        if (operationNumber == 0) {
          // addLast
          int randVal = StdRandom.uniform(0, 100);
          L.addLast(randVal);
          K.addLast(randVal);
        } else if (operationNumber == 1) {
          // size
          int sizeL = L.size();
          int sizeK = L.size();
          assertEquals(sizeK, sizeL);
        } else if (operationNumber == 2) {
            // getLast
            if (K.size() > 0 && L.size() > 0) {
                assertEquals(K.getLast(), L.getLast());
            }
        } else {
            // removeLast
            if (L.size() > 0 && L.size() > 0) {
                assertEquals(K.removeLast(), L.removeLast());
            }
        }
      }
    }
}
