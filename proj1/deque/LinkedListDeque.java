package deque;

import java.util.Iterator;

public class LinkedListDeque<T> {


    public class Node {
        private Node prev;
        private T item;
        private Node next;
        private Node(T data, Node bef, Node aft) {
            prev = bef;
            item = data;    // Do not add "T" before item
            next = aft;
        }
        /** constructor for Node.(especially for sentinel node). */
        public Node(Node bef, Node aft) {
            prev = bef;
            next = aft;
        }
    }
    private Node sentinel;
    private int size;
    public LinkedListDeque(T data) {
        size = 1;
        sentinel = new Node(null, null);
        sentinel.next = new Node(data, sentinel, sentinel);
        sentinel.next.next = sentinel;
        sentinel.prev = sentinel.next;
    }
    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null); // can not add 'Node' before sentinel
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }
    public void addFirst(T item) {
        Node first = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = first;
        sentinel.next = first;
        size++;
    }
    public void addLast(T item) {
        Node last = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = last;
        sentinel.prev = last;
        size++;
    }
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }
    public int size() {
        return size;
    }
    public void printDeque() {
        Node current = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(current.item);
            current = current.next;
            if (i < size - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T removedItem = sentinel.next.item;
            sentinel.next.next.prev = sentinel;
            sentinel.next = sentinel.next.next;
            size--;
            return removedItem;
        }
    }
    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T removedItem = sentinel.prev.item;
            sentinel.prev.prev.next = sentinel;
            sentinel.prev = sentinel.prev.prev;
            size--;
            return removedItem;
        }
    }
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        T indexedValue = null;
        for (int i = 0; i < index; i++) {
            Node current = sentinel.next;
            indexedValue = current.item;
        }
        return indexedValue;
    }
    private T getRecursiveHelp(Node track, int index) {
        if (index == 0) {
            return track.item;
        }
        return getRecursiveHelp(track.next, index - 1);
    }
    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveHelp(sentinel.next, index);
    }
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }
    private class LinkedListIterator implements Iterator<T> {
        private int position;
        public LinkedListIterator() {
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
        // instanceof casting not available under java 16.
        if (o instanceof LinkedListDeque) {
            if (this.size != ((LinkedListDeque) o).size) {
                return false;
            }
            // Loop and check if all items of this exist in o. Note that the orders of items must be same.
            for (int poz = 0; poz < this.size; poz++) {
                if (this.get(poz) != ((LinkedListDeque) o).get(poz)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    /** Check if LinkedList contains thing by using iterator. Useless method. */
    private boolean contains(T thing) {
        Iterator<T> AnIterator = this.iterator();
        while (AnIterator.hasNext()) {
            if (AnIterator.next() == thing) {
                return true;
            }
        }
        return false;
    }
}
