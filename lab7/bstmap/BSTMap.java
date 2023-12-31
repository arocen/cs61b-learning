package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K,V> {
    /** Launch an empty BSTmap. */
    private BSTNode root;             // root of BSTMap
    public BSTMap() {}
    private class BSTNode {
        private K key;                // sorted by key
        private V value;              // associated data
        private BSTNode left, right;  // left and right subtrees
        private int size;             // number of nodes in subtree
        public BSTNode(K key, V val, int size) {
            this.key = key;
            this.value = val;
            this.size = size;
        }
    }

    @Override
    /** Clear all mappings. */
    public void clear() {
        this.root = null;
    }
    @Override
    public boolean containsKey(K key) {
        /* Assume type K is int. */
        return get(key) != null;
    }
    @Override
    public V get(K key) {
        return get(root, key);
    }
    private V get(BSTNode x, K key) {
        if (key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else              return x.value;
    }
    @Override
    public int size() {
        return size(root);
    }
    private int size(BSTNode x) {
        if (x == null) return 0;
        else return x.size;
    }
    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }
    private BSTNode put(BSTNode x, K key, V val) {
        if (x == null) return new BSTNode(key, val, 1);
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = put(x.left,  key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else              x.value   = val;
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("keySet() is not available now!");
    }
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("remove() is not available now!");
    }
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("remove() is not available now!");
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("iterator() is not available now!");
    }
    /** Print out BSTMap in order of increasing Key */
    public void printInOrder() {
        throw new UnsupportedOperationException("printInOrder() is not available now!");
    }
}
