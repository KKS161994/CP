// LRUCache(int capacity) initializes the cache with a positive capacity.

// get(int key) returns the value if the key exists, otherwise -1.

// put(int key, int value) inserts or updates a key-value pair.

// When the cache exceeds its capacity, evict the least recently used key.

// Both get and put must run in O(1) average time complexity.
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LRUCache {

    public class Node {
        int key,value;
        Node prev, next;

        Node(int key,int value, Node prev, Node next) {
            this.key = key;
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node root, tail;
    private int capactiy;
    private Map<Integer, Node> keyToNodesMap;

    public LRUCache(int capacity) {
        this.root = new Node(0,0, null, null);
        this.tail = new Node(0,0, null, null);
        root.next = tail;
        tail.prev = root;
        this.capactiy = capacity;
        keyToNodesMap = new HashMap<>();
    }

    public int get(int key) {
        if (!keyToNodesMap.containsKey(key))
            return -1;
        Node node = keyToNodesMap.get(key);
        // update current node prev and next
        removeNodeFromCurrentPos(node);

        // add current node in end
        insertNodeAtEnd(node);
        return node.value;
    }

    private void removeNodeFromCurrentPos(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void insertNodeAtEnd(Node node) {
        tail.prev.next = node;
        node.prev = tail.prev;
        node.next = tail;
        tail.prev = node;
    }

    public void put(int key, int value) {
        Node node;
        if (!keyToNodesMap.containsKey(key)) {
            node = new Node(key,value, null, null);
            keyToNodesMap.put(key, node);
        } else {
            node = keyToNodesMap.get(key);
            node.value = value;
            removeNodeFromCurrentPos(node);
        }
        insertNodeAtEnd(node);
        while (keyToNodesMap.size() > capactiy) {
            Node temp = root.next;
            root.next = root.next.next;
            root.next.prev = root;
            keyToNodesMap.remove(temp.key);
        }
        
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);

        cache.put(1, 1);
        cache.put(2, 2);

        System.out.println(cache.get(1)); // 1

        cache.put(3, 3);
        System.out.println(cache.get(2)); // -1

        cache.put(4, 4);
        System.out.println(cache.get(1)); // -1
        System.out.println(cache.get(3)); // 3
        System.out.println(cache.get(4)); // 4
    }
}
