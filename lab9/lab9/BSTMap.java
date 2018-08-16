package lab9;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import static org.junit.Assert.*;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {


    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private Node getHelper(K key, Node p) {
        //throw new UnsupportedOperationException();
        if(p == null)
            return null;

        int compareInt = p.key.compareTo(key);
        if(compareInt == 0){
            return p;
        }else if(compareInt > 0){
            return getHelper(key, p.left);
        }else{
            return getHelper(key, p.right);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        //throw new UnsupportedOperationException();
        Node x = getHelper(key, root);
        if(x == null)
            return null;
        return x.value;
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        //throw new UnsupportedOperationException();
        if(p == null) {
            size += 1;
            return new Node(key, value);
        }

        int compareInt = p.key.compareTo(key);
        if(compareInt > 0){
            p.left = putHelper(key, value, p.left);
        }else if(compareInt < 0){
            p.right = putHelper(key, value, p.right);
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        //throw new UnsupportedOperationException();
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        //throw new UnsupportedOperationException();
        return size;
    }

    public static void main(String[] args){
        BSTMap<Integer, String> b = new BSTMap<>();
        b.put(1, "child");
        b.put(2, "parent");
        b.put(3, "husband");
        b.put(4, "wife");

        assertEquals(b.get(3), "husband");
        assertEquals(b.get(5), null);
        assertEquals(b.size(), 4);

        b.clear();
        assertEquals(b.size(), 0);
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        //throw new UnsupportedOperationException();
        return keySetHelper(new HashSet<>(), root);
    }

    private HashSet<K> keySetHelper(HashSet<K> set0, Node n){
        if(n != null){
            set0.add(n.key);
            set0 = keySetHelper(set0, n.left);
            set0 = keySetHelper(set0, n.right);
        }
        return set0;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */

    //--------------------supporting code for remove process-------------------------

    @Override
    public V remove(K key) {

        V removeV = null;
        Node toRemove = getHelper(key, root);

        if(toRemove != null) {
            removeV = toRemove.value;
            Node successor = extractSuccessor(toRemove);
            if (successor == null)
                if (toRemove == root) root = null;
            replace(toRemove, successor);
        }
        return removeV;
    }

    /*
    replace the previous node with the picked successor
    completely take the place of the previous node, including set its parent's child
    taking over its children.
     */
    private void replace(Node prev, Node successor){

        if(successor != null) {
            successor.left = prev.left;
            successor.right = prev.right;
        }

        Node prevParent = getParent(prev);
        if(prevParent != null){
            if(prevParent.right == prev)
                prevParent.right = successor;
            else
                prevParent.left = successor;
        }else {
            root = successor;
        }

    }

    /*
    pick an appropriate node and make it the successor, settle its child(literally only 1)
    if it returns null, then the vertex to remove is at the bottom of this tree
    in one case, it is root
     */
    private Node extractSuccessor(Node toRemove){
        Node left = getClosestNodeLeft(toRemove);
        Node right = getClosestNodeRight(toRemove);

        Node result = null;

        if(left == null && right ==  null)
            ;
        else if(right != null){                 //two cases:1 node being taken away is at its parent's right side, namely it has no left child
            Node parent = getParent(right);     //          2 at its parent's left side, it also has no left child
            if(parent.right == right)           //all need to be done is to replace the node with its right children(if has any)
                parent.right = right.right;
            else
                parent.left = right.right;
            result = right;
        }else if(left != null) {
            Node parent = getParent(left);
            if(parent.left == left)
                parent.left = left.left;
            else
                parent.right = left.left;
            result = left;
        }
        return result;
    }

    private Node getClosestNodeLeft(Node n){
        if(n.left == null)
            return null;
        else{
            Node x = n.left;
            while(x.right != null)
                x = x.right;
            return x;
        }
    }

    private Node getClosestNodeRight(Node n){
        if(n.right == null)
            return null;
        else{
            Node x = n.right;
            while(x.left != null)
                x = x.left;
            return x;
        }
    }

    private Node getParent(Node n){

        K thisKey = n.key;
        Node possibleParent = null;
        Node child = root;

        //make sure that Node n belongs to this map
        while(child != n){
            possibleParent = child;
            if(possibleParent.key.compareTo(thisKey) > 0)
                child = possibleParent.left;
            else
                child = possibleParent.right;
        }
        return possibleParent;
    }

    //--------------------supporting code for remove process-------------------------

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    public void printTree(){
        printTreeHelper("", root);
    }

    private void printTreeHelper(String indent, Node tree){
        if(tree != null){
            System.out.println(indent + tree.key);
            printTreeHelper(indent + "   ", tree.left);
            printTreeHelper(indent + "   ", tree.right);
        }
    }

}
