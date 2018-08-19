package lab9;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int hashcode = hash(key);
        return buckets[hashcode].get(key);
    }
    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        this.size = size() + 1;
        if(loadFactor()  > MAX_LF){
            resize();
            this.size = size() + 1;
        }
        int hashcode = hash(key);
        buckets[hashcode].put(key, value);
    }

    private void resize(){
        ArrayMap<K, V>[] original = buckets;        //reserve the original buckets
        this.size = 0;
        buckets = new ArrayMap[2 * original.length];

        //initialize the new buckets
        for(int i =0; i < buckets.length; i += 1)
            buckets[i] = new ArrayMap<>();

        for(ArrayMap x : original){
            Iterator<K> iter = x.iterator();
            while(iter.hasNext()) {
                K temp = iter.next();
                put(temp, (V) x.get(temp));
            }
        }
    }



    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return this.size;
    }

    public static void main(String[] args){
        MyHashMap<Integer, String> aMap = new MyHashMap<>();
        aMap.put(12 , "Young");
        aMap.put(98, "Wen");
        String str = "Hello";
        try {
            for (int i = 0; i < 16; i += 1) {
                aMap.put(i + 112, "h" + str);
                str = str + "h";
            }
        }catch (Exception e){
            System.out.println(e);
        }

        //assertEquals(aMap.get(12), "Young");
        //assertEquals(aMap.size(), 2);
        assertEquals(aMap.get(112), "hHello");
        Iterator<Integer> keyIter = aMap.iterator();
        for(Integer x : aMap.keySet())
            assertEquals(x, keyIter.next());
        System.out.println(aMap.get(200));

        aMap.remove(130);

        aMap.remove(12, "Young");
        System.out.println(aMap.get(12));
        //assertEquals(aMap.get(12), "Young");

    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        HashSet<K> result = new HashSet<>();
        for(ArrayMap x : buckets){
            Iterator<K> iter = x.iterator();
            while(iter.hasNext())
                result.add(iter.next());
        }
        return result;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        V returnV = get(key);
        int hashNum = hash(key);
        buckets[hashNum].remove(key);
        return returnV;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        if(get(key) == value)
            return remove(key);
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
