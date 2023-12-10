package deque;

import java.util.Iterator;

public class ArrayDeque<T> {
    private T[] list;
    private int size;
    /** Create an empty array list. */
    public static final int MIN_SIZE = 16;
    public ArrayDeque() {
        list = (T[]) new Object[MIN_SIZE];
        size = 0;
    }
    public int size() {
        return size;
    }
    public void addFirst(T item) {
        if (size == list.length) {
            resize(2 * list.length);
        }
        for (int i = size - 1; i > 0; i--) {
            list[i + 1] = list[i];
        }
        list[0] = item;
        size++;
    }
    public void addLast(T item) {
        if (size == list.length) {
            resize(2 * list.length);
        }
        list[size] = item;
        size++;
    }
    private void resize(int newLength) {
        T[] newList = (T[]) new Object[newLength];
        System.arraycopy(list, 0, newList, 0, size);
        list = newList;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(list[i]);
            if (i < size - 1) {
                System.out.print(" ");
            }
        }
        System.out.print("\n");
    }
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T removed = list[0];
        for (int i = 0; i < size - 1; i++) {
            list[i] = list[i + 1];
        }
        list[size - 1] = null;
        size--;
        if (size < 0.25 * list.length && list.length >= MIN_SIZE) {
            resize(list.length / 2); // list.length is always even.
        }
        return removed;
    }
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T removed = list[size - 1];
        list[size - 1] = null;
        size--;
        if (size < 0.25 * list.length && list.length >= MIN_SIZE) {
            resize(list.length / 2); // list.length is always even.
        }
        return removed;
    }
    public T get(int index) {
        if (index > size - 1) {
            return null;
        }
        return list[index];
    }
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }
    private class ArrayIterator implements Iterator {
        private int position;
        public ArrayIterator() {
            position = 0;
        }
        public boolean hasNext() {
            return position < size;
        }
        public T next() {
            T returnedItem = get(position);
            position += 1;
            return returnedItem;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ArrayDeque) {
            if (this.size != ((ArrayDeque) o).size) {
                return false;
            }
            for (int poz = 0; poz < this.size; poz++) {
                if (this.get(poz) != ((ArrayDeque) o).get(poz)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
