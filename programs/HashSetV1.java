import java.util.List;
import java.util.Map;

/**
 * MyHashSet implemented with separate chaining (buckets).
 *
 * Python original:
 *   size = 1009
 *   buckets = defaultdict(list)
 *   _hash(key)        -> key % size
 *   _get_bucket(key)  -> buckets[_hash(key)]
 *   add / remove / contains operate on the per-index list bucket.
 *
 * Faithful Java analog: a map of bucket-index -> list of keys (defaultdict(list)),
 * with the same modulo hashing and linear scan within a bucket.
 *
 * NOTE: This file is named HashSetV1, so java.util.HashSet is NOT imported.
 *       java.util.HashMap / ArrayList are used (fully qualified where needed).
 * NOTE: The Python `contains` had no explicit `return False` on the miss path
 *       (it implicitly returned None). Completed here to return false.
 */
public class HashSetV1 {

    static class MyHashSet {
        private final int size;
        private final Map<Integer, List<Integer>> buckets;

        public MyHashSet() {
            this.size = 1009;
            this.buckets = new java.util.HashMap<>();
        }

        private int hash(int key) {
            return key % size;
        }

        private List<Integer> getBucket(int key) {
            int index = hash(key);
            // Mirrors defaultdict(list): create an empty bucket on first access.
            return buckets.computeIfAbsent(index, k -> new java.util.ArrayList<>());
        }

        public void add(int key) {
            List<Integer> bucket = getBucket(key);
            if (!bucket.contains(key)) {
                bucket.add(key);
            }
        }

        public void remove(int key) {
            List<Integer> bucket = getBucket(key);
            // Integer.valueOf to remove by value, not by index.
            bucket.remove(Integer.valueOf(key));
        }

        public boolean contains(int key) {
            List<Integer> bucket = getBucket(key);
            return bucket.contains(key);
        }
    }

    public static void main(String[] args) {
        MyHashSet s = new MyHashSet();
        s.add(1);
        s.add(1010); // 1010 % 1009 == 1 -> same bucket as key 1
        System.out.println(s.contains(1));    // true
        System.out.println(s.contains(1010)); // true
        System.out.println(s.contains(2));    // false
        s.remove(1);
        System.out.println(s.contains(1));    // false
        System.out.println(s.contains(1010)); // true
    }
}
