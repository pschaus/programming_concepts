package algorithms;

/**
 * Question:
 *
 * Below you can find the Hashmap implementation from the syllabus; however,
 * this implementation only allows adding and getting elements.
 * You must implement the delete function that allows removing elements from the map.
 *
 * Once it is done, copy-paste the complete class, with the imports, in Inginious.
 *
 *
 * A Hash table generalizes arrays by allowing indexes with other types than integers;
 * for example, Strings as in this class.
 * To do so, a integer value is computed from the String, called a hash, and then put
 * back in the ranges of the array with a modulo.
 * This does not ensure that different elements go into different cells; hence each cell hash
 * a linked list to store the elements.
 * Hence, the methods in the maps are expected to be in O(1) for a good hash function.
 *
 * Your method delete must be in O(1) amortized complexity and O(n) in the worst case,
 * with n the number of elements in the map.
 *
 * Feel free to add methods or fields in the class but do not modify
 * the signature and behavior of existing code
 *
 */
import java.util.LinkedList;

public class TreeHashtable {

    // Size of the internal array
    private static final int N = 10;

    // Each entry in the array is a LinkedList to handle collisions
    private LinkedList<Entry>[] table;

    // Constructor initializes the array
    @SuppressWarnings("unchecked")
    public TreeHashtable() {
        table = new LinkedList[N];
        for (int i = 0; i < N; i++) {
            table[i] = new LinkedList<>();
        }
    }

    // A private class representing a key-value pair
    private static class Entry {
        String key;
        Integer value;

        Entry(String key, Integer value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * Hash function to calculate the index for a given key
     *
     * @param key the key on which the hash must be computed
     * @return the index in which the value associated with the key must be stored
     */
    private int hash(String key) {
        return Math.abs(key.hashCode()) % N;
    }

    // Put method to insert or update a key-value pair
    /**
     * Insert a key-value pair in the map. If the key is already present
     * in the map, replace the value with the new one.
     *
     * @param key the key
     * @param value the value associated with the key
     */
    public void put(String key, Integer value) {
        int index = hash(key);
        LinkedList<Entry> bucket = table[index];

        // Check if the key already exists, if so, update its value
        for (Entry entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        // If the key doesn't exist, add a new Entry to the bucket
        bucket.add(new Entry(key, value));
    }

    // Get method to retrieve the value associated with a key
    /**
     * Get the value associated with the key.
     *
     * @param key the key
     * @return the value associated with the key or null if the key is not
     *          in the map
     */
    public Integer get(String key) {
        int index = hash(key);
        LinkedList<Entry> bucket = table[index];

        // Iterate through the bucket to find the key
        for (Entry entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        // Return null if the key is not found
        return null;
    }

    /**
     * Removes the value associated with a key from the map and returns it
     *
     * @param key the key
     * @return the value associated with they key or null if the key is not in the map
     */
    public Integer delete(String key) {
        // TODO
        // STUDENT return null;
        // BEGIN STRIP
        int index = hash(key);
        LinkedList<Entry> bucket = table[index];

        // Iterate through the bucket to find the key
        for (Entry entry : bucket) {
            if (entry.key.equals(key)) {
                Integer value = entry.value;
                bucket.remove(entry);
                return value;
            }
        }
        return null;
        // END STRIP
    }
}
