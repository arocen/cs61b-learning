package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T>{
    public Comparator cmp;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        cmp = c;
    }
    public T max() {}
    public T max(Comparator<T> c) {}
}
