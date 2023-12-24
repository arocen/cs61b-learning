package deque;

import java.util.Comparator;

/** This interface defines a method for determining equality of characters. */
public interface CharacterComparator extends Comparator {
    /** Returns true if characters are equal by rules of implementing class. */
    boolean equalChars(char x, char y);
}