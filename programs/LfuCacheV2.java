import java.util.*;

/**
 * LFU (Least Frequently Used) cache implementation.
 *
 * Each key maps to a Node tracking its frequency. Nodes of the same frequency
 * are kept in a doubly linked list ordered by recency. A frequency_to_node map
 * groups the lists by frequency, and min_frequency tracks the smallest frequency
 * currently present so eviction can find the least-frequently / least-recently
 * used node in O(1).
 *
 * Python original used collections.defaultdict(DoublyLinkedList) and dict.
 */
public class LfuCacheV2 {

    static class Node {
        int key;
        int value;
        int frequency;
        Node prev;
        Node next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.frequency = 1;
            this.prev = null;
            this.next = null;
        }
    }

    static class DoublyLinkedList {
        Node head;
        Node tail;

        DoublyLinkedList() {
            head = new Node(-1, -1);
            tail = new Node(-1, -1);
            head.next = tail;
            tail.prev = head;
        }
    }

    private final int capacity;
    private final Map<Integer, DoublyLinkedList> frequencyToNode;
    private final Map<Integer, Node> keyToNode;
    private int minFrequency;

    public LfuCacheV2(int capacity) {
        this.capacity = capacity;
        this.frequencyToNode = new HashMap<>();
        this.keyToNode = new HashMap<>();
        this.minFrequency = 0;
    }

    // Mimics Python defaultdict(DoublyLinkedList): auto-creates an empty list.
    private DoublyLinkedList listFor(int frequency) {
        return frequencyToNode.computeIfAbsent(frequency, k -> new DoublyLinkedList());
    }

    private void removeFromOld(Node node, int frequency) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        // update min frequency if asked element was only Least frequently element
        if (minFrequency == frequency) {
            DoublyLinkedList nodes = listFor(frequency);
            if (nodes.head.next == nodes.tail) {
                minFrequency += 1;
                frequencyToNode.remove(frequency);
            }
        }
    }

    private void addToNew(Node node, int frequency) {
        DoublyLinkedList nodes = listFor(frequency + 1);
        Node head = nodes.head;
        node.next = head.next;
        node.prev = head;
        head.next = node;
        node.next.prev = node;
    }

    public int get(int key) {
        if (!keyToNode.containsKey(key)) {
            return -1;
        }
        Node node = keyToNode.get(key);
        int frequency = node.frequency;
        // update node frequency
        node.frequency += 1;
        // remove from old frequency list
        removeFromOld(node, frequency);
        // add to new list
        addToNew(node, frequency);
        return node.value;
    }

    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }

        if (keyToNode.containsKey(key)) {
            Node node = keyToNode.get(key);
            node.value = value;
            get(key);
            return;
        } else {
            Node node = new Node(key, value);
            int currentCapacity = keyToNode.size();
            if (currentCapacity == capacity) {
                // remove least frequently / least recently used
                DoublyLinkedList nodes = listFor(minFrequency);
                Node head = nodes.head;
                Node tail = nodes.tail;
                Node tempNode = tail.prev; // LRU node
                // dereference old lru
                tempNode.prev.next = tail;
                tail.prev = tempNode.prev;
                keyToNode.remove(tempNode.key);
                if (head.next == tail) {
                    frequencyToNode.remove(minFrequency);
                }
            }
            keyToNode.put(key, node);
            minFrequency = 1;
            addToNew(node, 0);
        }
    }

    public static void main(String[] args) {
        LfuCacheV2 cache = new LfuCacheV2(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1)); // 1
        cache.put(3, 3);                   // evicts key 2
        System.out.println(cache.get(2)); // -1
        System.out.println(cache.get(3)); // 3
        cache.put(4, 4);                   // evicts key 1
        System.out.println(cache.get(1)); // -1
        System.out.println(cache.get(3)); // 3
        System.out.println(cache.get(4)); // 4
    }
}
