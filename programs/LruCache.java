import java.util.*;

/**
 * LRU (Least Recently Used) cache.
 *
 * Backed by an OrderedDict (insertion-ordered map). get/put move the touched
 * key to the end (most recently used); when the size exceeds capacity the
 * first (least recently used) entry is popped.
 *
 * Python original used collections.OrderedDict; the Java analog is LinkedHashMap.
 */
public class LruCache {

    private final LinkedHashMap<Integer, Integer> cache;
    private final int capacity;

    public LruCache(int capacity) {
        this.cache = new LinkedHashMap<>();
        this.capacity = capacity;
    }

    // LinkedHashMap analog of OrderedDict.move_to_end(key).
    private void moveToEnd(int key) {
        Integer value = cache.remove(key);
        cache.put(key, value);
    }

    public int get(int key) {
        if (!cache.containsKey(key)) {
            return -1;
        }
        moveToEnd(key);
        return cache.get(key);
    }

    // NOTE: The Python source had bugs: it tested `self.key` (undefined attribute)
    // instead of `key`, and called move_to_end before inserting. Translated to the
    // evident intent: refresh recency for existing keys, insert, then evict the
    // least-recently-used entry (pop first) when over capacity.
    public void put(int key, int value) {
        if (cache.containsKey(key)) {
            moveToEnd(key);
        }
        cache.put(key, value);
        if (cache.size() > capacity) {
            int firstKey = cache.keySet().iterator().next(); // least recently used
            cache.remove(firstKey);
        }
    }

    public static void main(String[] args) {
        LruCache cache = new LruCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1)); // 1
        cache.put(3, 3);                   // evicts key 2
        System.out.println(cache.get(2)); // -1
        cache.put(4, 4);                   // evicts key 1
        System.out.println(cache.get(1)); // -1
        System.out.println(cache.get(3)); // 3
        System.out.println(cache.get(4)); // 4
    }
}
