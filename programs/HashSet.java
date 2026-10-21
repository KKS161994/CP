import java.util.Map;

/**
 * MyHashSet implemented with a defaultdict(bool) in Python.
 *
 * In Python:
 *   present = defaultdict(bool)
 *   add(key)      -> present[key] = True
 *   remove(key)   -> del present[key]
 *   contains(key) -> present[key]   (defaults to False when missing)
 *
 * Faithful Java analog: backing map of key -> Boolean, mirroring the
 * defaultdict(bool) semantics where a missing key reads as False.
 *
 * NOTE: This file is named HashSet, so java.util.HashSet is NOT imported.
 *       java.util.LinkedHashMap is used (fully qualified) as the backing map.
 */
public class HashSet {

    static class MyHashSet {
        private final Map<Integer, Boolean> present;

        public MyHashSet() {
            this.present = new java.util.LinkedHashMap<>();
        }

        public void add(int key) {
            present.put(key, true);
        }

        public void remove(int key) {
            // Mirrors Python's `del self.present[key]`.
            present.remove(key);
        }

        public boolean contains(int key) {
            // Mirrors defaultdict(bool): missing key -> False.
            return present.getOrDefault(key, false);
        }
    }

    public static void main(String[] args) {
        MyHashSet s = new MyHashSet();
        s.add(1);
        s.add(2);
        System.out.println(s.contains(1)); // true
        System.out.println(s.contains(3)); // false
        s.remove(2);
        System.out.println(s.contains(2)); // false
    }
}
