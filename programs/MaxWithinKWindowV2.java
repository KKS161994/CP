import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Sliding window maximum (front-insertion variant: "left to right decreasing").
 *
 * This is the mirror image of the standard monotonic-deque approach. Instead of
 * appending new indices to the back, new indices are inserted at the FRONT, so
 * the deque stays decreasing from left to right with the maximum's index at the
 * BACK (dq[-1] / peekLast).
 *
 * NOTE: The Python source for this variant did not run as written and was
 *       completed minimally to a correct, compiling equivalent of its evident
 *       intent. Fixes:
 *         - `res = List()` (invalid) -> a proper result list.
 *         - Window-eviction check compared the front (`dq[-1]`, the max index)
 *           but popped via `dq.pop()` (back). Since this variant keeps the
 *           oldest index at the back, eviction must inspect AND remove the back
 *           index when it leaves the window: `if dq and dq[-1] <= i-k: dq.pop()`.
 *         - The "while smaller" loop compared `nums[dq[0]]` (newest, front) and
 *           popped the front; this is corrected to pop newer-but-smaller indices
 *           from the FRONT so the front insertion preserves the decreasing order.
 *         - The result must record the windowed maximum value `nums[dq[-1]]`
 *           (the original returned the bare index `dq[-1]`).
 *       The net behavior matches the standard sliding-window-maximum (see
 *       MaxWithinKWindow), just built with front insertion.
 */
public class MaxWithinKWindowV2 {

    public static List<Integer> maxWithinKWindowV2(int[] nums, int k) {
        Deque<Integer> dq = new ArrayDeque<>();
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            // Oldest index lives at the back; evict it once it leaves the window.
            if (!dq.isEmpty() && dq.peekLast() <= i - k) {
                dq.pollLast();
            }
            // Maintain decreasing-left-to-right order: drop front indices whose
            // values are smaller than the incoming value before front-inserting.
            while (!dq.isEmpty() && nums[dq.peekFirst()] < nums[i]) {
                dq.pollFirst();
            }
            dq.addFirst(i);
            if (i >= k - 1) {
                res.add(nums[dq.peekLast()]);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        System.out.println(maxWithinKWindowV2(nums, k)); // [3, 3, 5, 5, 6, 7]
    }
}
