import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MergeKSortedList {

    static class Node {
        int val;
        Node next;

        Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }
    }

    public static Node sortKLinkedListNodes(Node[] lists) {
        PriorityQueue<Node> sortQueue = new PriorityQueue<>(
                (Node a, Node b) -> Integer.compare(a.val, b.val));
        for (Node head : lists) {
            if (head != null) {
                sortQueue.offer(head);
            }
        }
        Node dummy = new Node(0, null);
        Node tail = dummy;
        while (!sortQueue.isEmpty()) {
            Node current = sortQueue.poll();
            tail.next = current;
            tail = current;
            if (current.next != null) {
                sortQueue.add(current.next);
            }

        }
        tail.next = null;
        return dummy.next;

    }

    public static Node sortKLinkedListNodesv1(Node[] lists) {
        PriorityQueue<Node> sortQueue = new PriorityQueue<>(
                (Node a, Node b) -> Integer.compare(a.val, b.val));
        for (Node head : lists) {
            if (head != null) {
                sortQueue.offer(head);
            }
        }
        Node dummy = new Node(0, null);
        Node tail = dummy;
        while (!sortQueue.isEmpty()) {
            Node current = sortQueue.poll();
            tail.next = new Node(current.val, null);
            tail = tail.next;
            if (current.next != null) {
                sortQueue.add(current.next);
            }

        }
        tail.next = null;
        return dummy.next;

    }

}
