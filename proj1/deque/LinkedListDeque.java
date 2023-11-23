package deque;

public class LinkedListDeque<T> {


    public class Node{
        private Node prev;
        private T item;
        private Node next;
        private Node(T data, Node bef, Node aft){
            prev = bef;
            item = data;    // Do not add "T" before item
            next = aft;
        }
        /** constructor for Node.(especially for sentinel node). */
        public Node(Node bef, Node aft){
            prev = bef;
            next = aft;
        }
    }
    public Node sentinel;
    public int size;
    public LinkedListDeque(T data){
        size = 1;
        sentinel = new Node(null, null);
        sentinel.next = new Node(data, sentinel, sentinel);
        sentinel.next.next = sentinel;
        sentinel.prev = sentinel.next;
    }
    public LinkedListDeque(){
        size = 0;
        sentinel = new Node(null, null); // can not add 'Node' before sentinel
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }
    public void addFirst(T item){
        Node first = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = first;
        sentinel.next = first;
        size++;
    }
    public void addLast(T item){
        Node last = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = last;
        sentinel.prev = last;
        size++;
    }
    public boolean isEmpty(){
        if (size == 0){
            return true;
        }
        else {
            return false;
        }
    }
    public int size() {
        return size;
    }
    public void printDeque(){
        Node current = sentinel.next;
        for (int i = 0; i < size; i++){
            System.out.print(current.item);
            current = current.next;
            if (i < size - 1){
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}
