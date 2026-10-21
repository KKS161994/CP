import java.util.*;

/**
 * All O`one Data Structure (LFU-style frequency buckets via doubly-linked list).
 *
 * Implements inc / dec / getMaxKey / getMinKey, each in O(1) average time.
 *
 * This variant cleans the source node first (returning the new "current" node
 * after unlinking), then inserts into the destination tier.
 */
public class AllInOne {

    static class Node {
        int frequency;
        Set<String> keys = new HashSet<>();
        Node prev = null;
        Node next = null;

        Node() {
            this(0);
        }

        Node(int frequency) {
            this.frequency = frequency;
        }
    }

    static class DLL {
        Node head;
        Node tail;

        DLL() {
            head = new Node();
            tail = new Node();
            head.next = tail;
            tail.prev = head;
        }
    }

    static class AllOne {
        DLL dll;
        Map<Integer, Node> countToNode;
        Map<String, Integer> keyToFrequency; // defaultdict(int) -> getOrDefault 0

        AllOne() {
            dll = new DLL();
            countToNode = new HashMap<>();
            keyToFrequency = new HashMap<>();
        }

        Node cleanNodeWithNoKeys(Node node) {
            if (node.keys.isEmpty()) {
                countToNode.remove(node.frequency);
                Node prevNode = node.prev;
                prevNode.next = node.next;
                node.next.prev = prevNode;
                node = prevNode;
            }
            return node;
        }

        void addBefore(Node node, Node newNode) {
            Node prevNode = node.prev;
            newNode.prev = prevNode;
            newNode.next = node;
            node.prev = newNode;
            prevNode.next = newNode;
        }

        void addAfter(Node node, Node newNode) {
            Node nextNode = node.next;
            newNode.prev = node;
            newNode.next = nextNode;
            node.next = newNode;
            nextNode.prev = newNode;
        }

        public void inc(String key) {
            int frequency = keyToFrequency.getOrDefault(key, 0);
            Node node;

            if (frequency > 0) {
                node = countToNode.get(frequency);
                node.keys.remove(key);
                node = cleanNodeWithNoKeys(node);
            } else {
                node = dll.head;
            }

            if (!countToNode.containsKey(frequency + 1)) {
                Node newNode = new Node(frequency + 1);
                addAfter(node, newNode);
                countToNode.put(frequency + 1, newNode);
            }

            Node newNode = countToNode.get(frequency + 1);
            newNode.keys.add(key);
            keyToFrequency.put(key, frequency + 1);
        }

        public void dec(String key) {
            int frequency = keyToFrequency.get(key);
            Node node = countToNode.get(frequency);
            node.keys.remove(key);
            node = cleanNodeWithNoKeys(node);
            if (frequency > 1) {
                if (!countToNode.containsKey(frequency - 1)) {
                    Node newNode = new Node(frequency - 1);
                    addBefore(node, newNode);
                    countToNode.put(frequency - 1, newNode);
                }

                Node newNode = countToNode.get(frequency - 1);
                newNode.keys.add(key);
                keyToFrequency.put(key, frequency - 1);
            } else {
                keyToFrequency.remove(key);
            }
        }

        public String getMaxKey() {
            if (dll.head.next == dll.tail) {
                return "";
            }
            Node node = dll.tail.prev;
            return node.keys.iterator().next();
        }

        public String getMinKey() {
            if (dll.head.next == dll.tail) {
                return "";
            }
            Node node = dll.head.next;
            return node.keys.iterator().next();
        }
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
