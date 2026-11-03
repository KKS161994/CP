
import java.util.*;


public class MergeKLists {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    public ListNode mergeKLists(ListNode[] lists) {
        if( lists== null || lists.length == 0) return null;
        // Your implementation
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>(
            (a,b) -> Integer.compare(a.val,b.val)
        );
        ListNode result = new ListNode(0);
        ListNode tempResult = result;
        for(ListNode node: lists){
            if(node == null) continue;
            minHeap.offer(node);
        }

        while(!minHeap.isEmpty()){
            ListNode tempNode = minHeap.poll();
            ListNode tempRes = new ListNode(tempNode.val);
            tempResult.next = tempRes;
            tempResult = tempRes;
            if(tempNode.next!=null) minHeap.offer(tempNode.next);

        }

        return result.next;
    }

    private static ListNode buildList(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        for (int value : values) {
            tail.next = new ListNode(value);
            tail = tail.next;
        }

        return dummy.next;
    }

    private static void printList(ListNode head) {
        while (head != null) {
            System.out.print(head.val);
            if (head.next != null) System.out.print(" -> ");
            head = head.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        MergeKLists solution = new MergeKLists();

        ListNode[] lists = {
            buildList(1, 4, 5),
            buildList(1, 3, 4),
            buildList(2, 6)
        };

        printList(solution.mergeKLists(lists));
        // Expected: 1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6

        printList(solution.mergeKLists(new ListNode[]{}));
        // // Expected: empty

        printList(solution.mergeKLists(new ListNode[]{null}));
        // Expected: empty
    }
}
