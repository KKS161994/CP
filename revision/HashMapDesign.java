import java.util.LinkedList;

class MyHashMap {
    // Bucket array. Prime size reduces clustering for the key % size hash.
    private static final int NUM_BUCKETS = 769;
    private LinkedList<int[]>[] buckets;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        buckets = new LinkedList[NUM_BUCKETS];
        for (int i = 0; i < NUM_BUCKETS; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    // Hash function: maps any key into a bucket index.
    // Math.floorMod handles negative keys correctly (unlike %).
    private int hash(int key) {
        return Math.floorMod(key, NUM_BUCKETS);
    }

    public void put(int key, int value) {
        LinkedList<int[]> bucket = buckets[hash(key)];
        // Collision handling: scan the chain for an existing key.
        for (int[] entry : bucket) {
            if (entry[0] == key) {
                entry[1] = value; // update in place
                return;
            }
        }
        bucket.add(new int[]{key, value}); // new key -> append to chain
    }

    public int get(int key) {
        LinkedList<int[]> bucket = buckets[hash(key)];
        for (int[] entry : bucket) {
            if (entry[0] == key) {
                return entry[1];
            }
        }
        return -1; // not found
    }

    public void remove(int key) {
        LinkedList<int[]> bucket = buckets[hash(key)];
        for (int[] entry : bucket) {
            if (entry[0] == key) {
                bucket.remove(entry);
                return;
            }
        }
    }
}
