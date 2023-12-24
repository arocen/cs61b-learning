package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    /** ComparatorA used to test MaxArrayDeque. */
    private class ComparatorA implements CharacterComparator {

        public boolean equalChars(char x, char y) {
            if (x < y) {
                System.out.println(x + " comes before " + y);
            } else if (x > y) {
                System.out.println(x + " comes after " + y);
            } else {
                System.out.println(x + " is equal to " + y);
            }
        }

    }
    @Test
    public void test1() {
        Comparator cmpA = new ComparatorA();
        MaxArrayDeque<Character> mad1 = new MaxArrayDeque<Character>(cmpA);
    }
}
