import java.util.*;

public class AllOne {
    private Map<String, Integer> keyToCount;
    private Map<Integer, Node> countToNode;
    Node head;
    int max = 0;

    public class Node {

        Set<String> values;
        Node next, prev;
        int count;

        Node(int count) {
            this.count = count;
            values = new HashSet<>();
        }
    }

    public AllOne() {
        // Initialize your data structure
        keyToCount = new HashMap<>();
        countToNode = new HashMap<>();
        head = new Node(0);
    }

    public void inc(String key) {
        // Implement
        int count = keyToCount.getOrDefault(key, 0);
        keyToCount.put(key, count + 1);
        Node currentNode = countToNode.get(count);

        // Add value to new node
        if (!countToNode.containsKey(count + 1)) {
            countToNode.put(count + 1, new Node(count + 1));
            Node newNode = countToNode.get(count + 1);
            if (count == 0) {
                newNode.next = head.next;
                if(head.next != null) head.next.prev = newNode;
                head.next = newNode;
                newNode.prev = head;
                
            } else {
                newNode.next = currentNode.next;
                if (currentNode.next != null)
                    currentNode.next.prev = newNode;
                currentNode.next = newNode;
                newNode.prev = currentNode;
            }
        }
        countToNode.get(count + 1).values.add(key);

        max = Math.max(max, count + 1);

        // remove key from existing node
        if (count != 0) {
            Node prevNode = countToNode.get(count);
            Set<String> set = prevNode.values;
            set.remove(key);
            // if removing key makes set empty remove node
            if (set.size() == 0) {
                prevNode.prev.next = prevNode.next;
                if (prevNode.next != null)
                    prevNode.next.prev = prevNode.prev;
                countToNode.remove(count);
            }

        }
    }

    public void dec(String key) {
        int count = keyToCount.getOrDefault(key, 0);
        if (count == 0)
            return;
        Node node = countToNode.get(count);
        if (count == 1) {
            keyToCount.remove(key);
        } else {
            if (!countToNode.containsKey(count - 1)) {
                countToNode.put(count - 1, new Node(count - 1));
                Node newNode = countToNode.get(count - 1);
                newNode.next = node;
                newNode.prev = node.prev;
                node.prev = newNode;
                newNode.prev.next = newNode;
            }
            countToNode.get(count - 1).values.add(key);
            keyToCount.put(key, count - 1);
        }
        node.values.remove(key);
        if (node.values.isEmpty()) {
            if (max == count) {
                max = node.prev.count;
            }
            node.prev.next = node.next;
            countToNode.remove(count);
            if (node.next != null) {
                node.next.prev = node.prev;
            }
        }
    }

    public String getMaxKey() {
        // Implement
        if (head.next == null || max == 0) {
            return "";
        }
        return countToNode.get(max).values.iterator().next();

    }

    public String getMinKey() {
        // Implement
        if (head.next == null || max == 0) {
            return "";
        }
        return head.next.values.iterator().next();
    }

    private static void check(String name, boolean condition) {
        System.out.println(
                (condition ? "PASS" : "FAIL") + " | " + name);
    }

    public static void main(String[] args) {
        AllOne ds = new AllOne();

        // Test 1: Empty data structure
        check("Empty max", ds.getMaxKey().equals(""));
        check("Empty min", ds.getMinKey().equals(""));

        // Test 2: Single key
        ds.inc("hello");
        ds.inc("hello");

        check("Single max", ds.getMaxKey().equals("hello"));
        check("Single min", ds.getMinKey().equals("hello"));

        // Test 3: Different counts
        ds.inc("leet");

        check("Max hello", ds.getMaxKey().equals("hello"));
        check("Min leet", ds.getMinKey().equals("leet"));

        // Test 4: Equal counts
        ds.dec("hello");

        Set<String> valid = Set.of("hello", "leet");

        check("Equal max", valid.contains(ds.getMaxKey()));
        check("Equal min", valid.contains(ds.getMinKey()));

        // Test 5: Remove a key
        ds.dec("leet");

        check("After removal max",
                ds.getMaxKey().equals("hello"));
        check("After removal min",
                ds.getMinKey().equals("hello"));

        // Test 6: Remove last key
        ds.dec("hello");

        check("Empty after removal",
                ds.getMaxKey().equals("") &&
                        ds.getMinKey().equals(""));

        // Test 7: Repeated updates
        ds.inc("a");
        ds.inc("b");
        ds.inc("b");
        ds.inc("c");
        ds.inc("c");
        ds.inc("c");

        check("Max c", ds.getMaxKey().equals("c"));
        check("Min a", ds.getMinKey().equals("a"));

        ds.dec("c");
        ds.dec("c");

        check("Max b", ds.getMaxKey().equals("b"));
        check("Min a or c",
        Set.of("a", "c").contains(ds.getMinKey()));
    }
}
