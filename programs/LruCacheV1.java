import java.util.*;

/**
 * LRU (Least Recently Used) cache, v1.
 *
 * Backed by a deque of Nodes (recency order, most-recently-used at the back)
 * plus a map from key to Node. get/put move the touched node to the back; when
 * size exceeds capacity the front (least recently used) node is evicted.
 *
 * Python original used collections.deque and dict.
 */
public class LruCacheV1 {

    static class Node {
        int key;
        int value;
    }

    private final Deque<Node> deque;
    private final Map<Integer, Node> map;
    private final int capacity;

    public LruCacheV1(int capacity) {
        this.deque = new ArrayDeque<>();
        this.map = new HashMap<>();
        this.capacity = capacity;
    }

    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        Node node = map.get(key);
        deque.remove(node);
        deque.addLast(node);
        return node.value;
    }

    // NOTE: The Python source was buggy: it always created a fresh Node and called
    // self.map.put(...) (dict has no .put), and removed a node not yet in the deque
    // on the insert path. Reconstructed to the evident intent of an LRU put:
    // insert new keys (evicting LRU when over capacity) and refresh existing keys.
    public void put(int key, int value) {
        Node node;
        if (!map.containsKey(key)) {
            node = new Node();
            node.key = key;
            node.value = value;
            map.put(key, node);
        } else {
            node = map.get(key);
            deque.remove(node);
        }
        deque.addLast(node);
        node.value = value;
        if (deque.size() > capacity) {
            Node evicted = deque.pollFirst();
            if (evicted != null) {
                map.remove(evicted.key);
            }
        }
    }

    public static void main(String[] args) {
        LruCacheV1 cache = new LruCacheV1(2);
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
