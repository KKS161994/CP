
public class ReverseNodesInKGroup {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = head;
        int i = 0;
        ListNode newRoot = new ListNode(0);
        ListNode tempRoot = newRoot;
        ListNode tempDummy = null;
        ListNode newDummy = null;
        ListNode newTemp=null;

        while (dummy != null) {
            ListNode temp = dummy;
            for (i = 0; i < k && temp != null; i++) {
                temp = temp.next;
            }
            if (i != k) {
                tempRoot.next = dummy;
                return newRoot.next;
            }
            tempDummy = dummy;
            while (dummy != temp) {
                newTemp = dummy;
                dummy = dummy.next;
                newTemp.next = newDummy;
                newDummy = newTemp;
            }
            tempRoot.next = newDummy;
            tempRoot = tempDummy; 
            newDummy = null;           
        }
        return newRoot.next;
    }

    private static ListNode build(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;

        for (int value : values) {
            tail.next = new ListNode(value);
            tail = tail.next;
        }

        return dummy.next;
    }

    private static void test(int k, int[] input, String expected) {
        ReverseNodesInKGroup sol = new ReverseNodesInKGroup();
        ListNode result = sol.reverseKGroup(build(input), k);

        StringBuilder sb = new StringBuilder();
        int count = 0;

        while (result != null && count < 100) {
            if (count > 0)
                sb.append(" -> ");
            sb.append(result.val);
            result = result.next;
            count++;
        }

        String actual = sb.toString();
        System.out.printf("%s | k=%d | expected=%s | actual=%s%n",
                expected.equals(actual) ? "PASS" : "FAIL",
                k, expected, actual);
    }

    public static void main(String[] args) {
        test(2, new int[] { 1, 2, 3, 4 }, "2 -> 1 -> 4 -> 3");
        test(2, new int[] { 1, 2, 3, 4, 5 }, "2 -> 1 -> 4 -> 3 -> 5");
        test(3, new int[] { 1, 2, 3, 4, 5 }, "3 -> 2 -> 1 -> 4 -> 5");
        test(1, new int[] { 1, 2, 3 }, "1 -> 2 -> 3");
        test(5, new int[] { 1, 2, 3 }, "1 -> 2 -> 3");
        test(2, new int[] { 1, 2, 3, 4 }, "2 -> 1 -> 4 -> 3");
        test(2, new int[] {}, "");
    }
}
