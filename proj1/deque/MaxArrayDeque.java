package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    public Comparator cmp;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        cmp = c;
    }
    public T max() {
        if (this.size() == 0) {
            return null;
        }
        // Loop and compare to get maximum.
        int maxIndex = 0;
        int currentIndex = 0;
        while (currentIndex < this.size()) {
            if (cmp.compare(this.get(maxIndex), this.get(currentIndex)) < 0) {
                maxIndex = currentIndex;
            }
            currentIndex++;
        }
        return this.get(maxIndex);
    }
    public T max(Comparator<T> c) {
        if (this.size() == 0) {
            return null;
        }
        int maxIndex = 0;
        int currentIndex = 0;
        while (currentIndex < this.size()) {
            if (c.compare(this.get(maxIndex), this.get(currentIndex)) < 0) {
                maxIndex = currentIndex;
            }
            currentIndex++;
        }
        return this.get(maxIndex);
    }
}
