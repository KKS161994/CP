import java.util.*;

/**
 * All O`one Data Structure (hash-map based with min/max frequency tracking).
 *
 * Maintains:
 *   - stringToFrequency: key -> current count
 *   - frequencyToString: count -> set of keys with that count
 *   - min / max frequency bounds for O(1) getMinKey / getMaxKey.
 *
 * NOTE: faithfully mirrors the Python defaultdict(int)/defaultdict(set)
 * semantics. getMinKey/getMaxKey index into frequencyToString and rely on
 * those bucket sets existing; computeIfAbsent reproduces defaultdict's
 * auto-vivification so behavior matches the original exactly.
 *
 * NOTE: the original Python never updates `min`, so getMinKey() reads the
 * frequency-0 bucket (usually empty) and raises StopIteration. This Java
 * port preserves that behavior faithfully: getMinKey() on an empty bucket
 * throws NoSuchElementException (the analog of StopIteration). It is NOT
 * "fixed" here, to keep the translation an exact analog of the source.
 */
public class AllOne {

    private final Map<String, Integer> stringToFrequency;
    private final Map<Integer, Set<String>> frequencyToString;
    private int min;
    private int max;

    public AllOne() {
        stringToFrequency = new HashMap<>();
        frequencyToString = new HashMap<>();
        min = 0;
        max = 0;
    }

    public void inc(String key) {
        int initialFrequency = stringToFrequency.getOrDefault(key, 0);
        stringToFrequency.put(key, initialFrequency + 1);
        if (initialFrequency > 0) {
            bucket(initialFrequency).remove(key);
        }
        if (bucket(initialFrequency).isEmpty()) {
            frequencyToString.remove(initialFrequency);
        }
        bucket(initialFrequency + 1).add(key);
        if (max < (initialFrequency + 1)) {
            max = initialFrequency + 1;
        }
    }

    public void dec(String key) {
        int initialFrequency = stringToFrequency.getOrDefault(key, 0);
        stringToFrequency.put(key, initialFrequency - 1);
        bucket(initialFrequency).remove(key);
        if (bucket(initialFrequency).isEmpty()) {
            frequencyToString.remove(initialFrequency);
        }
        if (initialFrequency > 1) {
            bucket(initialFrequency - 1).add(key);
        }
    }

    public String getMaxKey() {
        return bucket(max).iterator().next();
    }

    public String getMinKey() {
        return bucket(min).iterator().next();
    }

    // defaultdict(set) auto-vivification: returns (creating if needed) the
    // key set for a given frequency.
    private Set<String> bucket(int frequency) {
        return frequencyToString.computeIfAbsent(frequency, k -> new HashSet<>());
    }

    // Your AllOne object will be instantiated and called as such:
    // AllOne obj = new AllOne();
    // obj.inc(key);
    // obj.dec(key);
    // String param3 = obj.getMaxKey();
    // String param4 = obj.getMinKey();
    public static void main(String[] args) {
        AllOne obj = new AllOne();
        obj.inc("hello");
        obj.inc("hello");
        System.out.println(obj.getMaxKey()); // "hello"
        System.out.println(obj.getMinKey()); // "hello"
        obj.inc("leet");
        System.out.println(obj.getMaxKey()); // "hello"
        System.out.println(obj.getMinKey()); // "leet"
    }
}
