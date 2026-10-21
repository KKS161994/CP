import java.util.*;

/**
 * LFU (Least Frequently Used) cache implementation, v3.
 *
 * A cleaner version: DoublyLinkedList encapsulates add_first / remove /
 * remove_last / is_empty operations. key_to_node maps keys to nodes,
 * freq_to_nodes groups nodes by frequency, and min_freq tracks the smallest
 * present frequency for O(1) eviction.
 *
 * Python original used collections.defaultdict(DoublyLinkedList) and dict.
 */
public class LfuV3 {

    static class Node {
        int key;
        int value;
        int freq;
        Node prev;
        Node next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
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

        void addFirst(Node node) {
            Node first = head.next;
            node.next = first;
            node.prev = head;
            head.next = node;
            first.prev = node;
        }

        void remove(Node node) {
            Node prevNode = node.prev;
            Node nextNode = node.next;
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
            node.prev = null;
            node.next = null;
        }

        Node removeLast() {
            if (isEmpty()) {
                return null;
            }
            Node node = tail.prev;
            remove(node);
            return node;
        }

        boolean isEmpty() {
            return head.next == tail;
        }
    }

    private final int capacity;
    private int minFreq;
    private final Map<Integer, Node> keyToNode;
    private final Map<Integer, DoublyLinkedList> freqToNodes;

    public LfuV3(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.keyToNode = new HashMap<>();
        this.freqToNodes = new HashMap<>();
    }

    // Mimics Python defaultdict(DoublyLinkedList): auto-creates an empty list.
    private DoublyLinkedList listFor(int freq) {
        return freqToNodes.computeIfAbsent(freq, k -> new DoublyLinkedList());
    }

    private void increaseFreq(Node node) {
        int oldFreq = node.freq;
        DoublyLinkedList oldList = listFor(oldFreq);

        oldList.remove(node);

        if (oldFreq == minFreq && oldList.isEmpty()) {
            minFreq += 1;
            freqToNodes.remove(oldFreq);
        }

        node.freq += 1;
        DoublyLinkedList newList = listFor(node.freq);
        newList.addFirst(node);
    }

    public int get(int key) {
        if (!keyToNode.containsKey(key)) {
            return -1;
        }
        Node node = keyToNode.get(key);
        increaseFreq(node);
        return node.value;
    }

    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }

        if (keyToNode.containsKey(key)) {
            Node node = keyToNode.get(key);
            node.value = value;
            increaseFreq(node);
            return;
        }

        if (keyToNode.size() == capacity) {
            DoublyLinkedList minFreqList = listFor(minFreq);
            Node nodeToRemove = minFreqList.removeLast();

            keyToNode.remove(nodeToRemove.key);

            if (minFreqList.isEmpty()) {
                freqToNodes.remove(minFreq);
            }
        }

        Node newNode = new Node(key, value);
        keyToNode.put(key, newNode);
        listFor(1).addFirst(newNode);
        minFreq = 1;
    }

    public static void main(String[] args) {
        LfuV3 cache = new LfuV3(2);
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
