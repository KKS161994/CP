import java.util.*;

/**
 * LC 432. All O`one Data Structure
 *
 * URL: https://leetcode.com/problems/all-oone-data-structure/
 * Difficulty: Hard
 * Tags: Hash Table, Linked List, Design, Doubly-Linked List
 *
 * Design a data structure to store the strings' count with the ability to return the strings with minimum and maximum counts.
 *
 * Implement the AllOne class:
 *
 *     - AllOne() Initializes the object of the data structure.
 *
 *     - inc(String key) Increments the count of the string key by 1. If key does not exist in the data structure, insert it with count 1.
 *
 *     - dec(String key) Decrements the count of the string key by 1. If the count of key is 0 after the decrement, remove it from the data structure. It is guaranteed that key exists in the data structure before the decrement.
 *
 *     - getMaxKey() Returns one of the keys with the maximal count. If no element exists, return an empty string "".
 *
 *     - getMinKey() Returns one of the keys with the minimum count. If no element exists, return an empty string "".
 *
 * Note that each function must run in O(1) average time complexity.
 *
 * Example 1:
 *
 * Input
 * ["AllOne", "inc", "inc", "getMaxKey", "getMinKey", "inc", "getMaxKey", "getMinKey"]
 * [[], ["hello"], ["hello"], [], [], ["leet"], [], []]
 * Output
 * [null, null, null, "hello", "hello", null, "hello", "leet"]
 *
 * Explanation
 * AllOne allOne = new AllOne();
 * allOne.inc("hello");
 * allOne.inc("hello");
 * allOne.getMaxKey(); // return "hello"
 * allOne.getMinKey(); // return "hello"
 * allOne.inc("leet");
 * allOne.getMaxKey(); // return "hello"
 * allOne.getMinKey(); // return "leet"
 *
 * Constraints:
 *
 *     - 1 <= key.length <= 10
 *
 *     - key consists of lowercase English letters.
 *
 *     - It is guaranteed that for each call to dec, key is existing in the data structure.
 *
 *     - At most 5 * 10^4 calls will be made to inc, dec, getMaxKey, and getMinKey.
 *
 */
public class Lc0432AllOOneDataStructure {

    static class Node {
        int frequency = 0;
        Set<String> keys = new HashSet<>();
        Node prev = null;
        Node next = null;
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
        Map<String, Integer> keyToCount;

        AllOne() {
            dll = new DLL();
            countToNode = new HashMap<>();
            keyToCount = new HashMap<>();
        }

        private void cleanNodeWithNoKeys(Node node) {
            if (node.keys.isEmpty()) {
                Node nodePrev = node.prev;
                node.next.prev = nodePrev;
                nodePrev.next = node.next;
                countToNode.remove(node.frequency);
                node = nodePrev;
            }
        }

        private void addAfter(Node node, Node newNode) {
            Node nextNode = node.next;
            newNode.next = nextNode;
            newNode.prev = node;
            node.next = newNode;
            nextNode.prev = newNode;
        }

        private void addBefore(Node node, Node newNode) {
            Node prevNode = node.prev;
            newNode.prev = prevNode;
            newNode.next = node;
            prevNode.next = newNode;
            node.prev = newNode;
        }

        public void inc(String key) {
            int frequency = keyToCount.getOrDefault(key, 0);
            Node node;
            if (frequency == 0) {
                node = dll.head;
            } else {
                node = countToNode.get(frequency);
            }

            if (!countToNode.containsKey(frequency + 1)) {
                Node newNode = new Node();
                newNode.frequency = frequency + 1;
                addAfter(node, newNode);
                countToNode.put(frequency + 1, newNode);
            }
            Node newNode = countToNode.get(frequency + 1);
            newNode.keys.add(key);
            keyToCount.put(key, frequency + 1);
            if (frequency != 0) {
                node.keys.remove(key);
                cleanNodeWithNoKeys(node);
            }
        }

        public void dec(String key) {
            int frequency = keyToCount.get(key);
            Node node = countToNode.get(frequency);
            if (frequency == 1) {
                keyToCount.remove(key);
                node.keys.remove(key);
                cleanNodeWithNoKeys(node);
                return;
            }

            if (!countToNode.containsKey(frequency - 1)) {
                Node newNode = new Node();
                newNode.frequency = frequency - 1;
                countToNode.put(frequency - 1, newNode);
                addBefore(node, newNode);
            }

            Node newNode = countToNode.get(frequency - 1);
            newNode.keys.add(key);
            node.keys.remove(key);
            cleanNodeWithNoKeys(node);
            keyToCount.put(key, frequency - 1);
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

    public static void main(String[] args) {
        AllOne allOne = new AllOne();
        allOne.inc("hello");
        allOne.inc("hello");
        System.out.println(allOne.getMaxKey()); // "hello"
        System.out.println(allOne.getMinKey()); // "hello"
        allOne.inc("leet");
        System.out.println(allOne.getMaxKey()); // "hello"
        System.out.println(allOne.getMinKey()); // "leet"
    }
}
