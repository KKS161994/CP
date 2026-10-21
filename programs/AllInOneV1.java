import java.util.*;

/**
 * Strong analysis. You've actually identified two distinct bug classes here, and both are exactly the kind of thing that distinguishes someone who's solved LFU/All-in-One mechanically from someone who understands why the architecture is hard.
 *
 * Let me reflect back what you found, because the insight matters:
 *
 * Bug 1: Source cleanup ordering.
 * You were doing source-cleanup-then-destination-add. The issue: when you remove from source, the source node might become empty. But the destination logic needs to know what's adjacent (prev/next nodes in the frequency DLL). Cleaning up source first changes the DLL structure, so destination logic operates on a different topology than expected.
 *
 * Bug 2: Adjacency assumption.
 * After removing a key from a node, you assumed the new previous node has frequency = current-1 (for dec) or current+1 (for inc). But that's only true if a node with that exact frequency exists. If the frequency tier doesn't exist yet (e.g., you decrement from freq=3 but no freq=2 node exists), you can't just attach the key to "the previous node" — you need a new node.
 *
 * The architectural fix you made: destination-first, source-cleanup-after.
 *
 * This is exactly right and it's the standard approach in production LFU implementations (Caffeine library does this, for instance). The reason it works:
 *
 * 1. First, determine where the key SHOULD go (target frequency tier)
 * 2. Create that tier if it doesn't exist
 * 3. Add the key to the target tier
 * 4. Then remove from source tier
 * 5. If source tier is now empty, clean it up
 *
 * The order matters because step 1-3 don't depend on source state, while source cleanup (step 4-5) is straightforward once the key is safely in its new home.
 *
 * The two edge cases you mentioned:
 *
 * Inc when no existing frequency tier: if key is brand new (inc from 0), there's no source to clean up. Source cleanup logic must be guarded by "did this key exist before?"
 *
 * Dec when node becomes 1: if the only key in the node moves out, the node becomes empty and should be removed. But you shouldn't create a "frequency 0" node to receive removed keys — keys with count 0 are simply deleted from the data structure entirely.
 *
 * These two edge cases are exactly where most LFU implementations have bugs. You found them by reasoning, not by hitting failing test cases. That's the senior-engineer pattern.
 */
public class AllInOneV1 {

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

        void cleanNodeWithNoKeys(Node node) {
            if (node.keys.isEmpty()) {
                countToNode.remove(node.frequency);
                Node prevNode = node.prev;
                prevNode.next = node.next;
                node.next.prev = prevNode;
            }
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
            if (frequency == 0) {
                node = dll.head;
            } else {
                node = countToNode.get(frequency);
            }
            if (!countToNode.containsKey(frequency + 1)) {
                Node newNode = new Node(frequency + 1);
                addAfter(node, newNode);
                countToNode.put(frequency + 1, newNode);
            }

            Node newNode = countToNode.get(frequency + 1);
            newNode.keys.add(key);
            keyToFrequency.put(key, frequency + 1);

            // Clean
            if (frequency != 0) {
                node.keys.remove(key);
                cleanNodeWithNoKeys(node);
            }
        }

        public void dec(String key) {
            int frequency = keyToFrequency.get(key);
            Node node = countToNode.get(frequency);
            // Assuming atleast once key is present
            if (frequency == 1) {
                keyToFrequency.remove(key);
                node.keys.remove(key);
                cleanNodeWithNoKeys(node);
                return;
            }

            if (!countToNode.containsKey(frequency - 1)) {
                Node newNode = new Node(frequency - 1);
                addBefore(node, newNode);
                countToNode.put(frequency - 1, newNode);
            }
            Node newNode = countToNode.get(frequency - 1);
            newNode.keys.add(key);
            node.keys.remove(key);
            cleanNodeWithNoKeys(node);
            keyToFrequency.put(key, frequency - 1);
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
