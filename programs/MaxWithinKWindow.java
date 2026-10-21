import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Sliding window maximum.
 *
 * Maintains a monotonic (decreasing) deque of indices. The front of the deque
 * always holds the index of the maximum element of the current window. Indices
 * that fall out of the window (front) are dropped, and indices whose values are
 * smaller than the incoming value (back) are popped before appending.
 *
 * Python original:
 *   dq = deque()
 *   for i in range(len(nums)):
 *       if dq and dq[0] <= i-k: dq.popleft()
 *       while dq and nums[dq[-1]] < nums[i]: dq.pop()
 *       dq.append(i)
 *       if i >= k-1: res.append(nums[dq[0]])
 *
 * Faithful Java analog: ArrayDeque<Integer> of indices, ArrayList<Integer> result.
 */
public class MaxWithinKWindow {

    public static List<Integer> maxWithinK(int[] nums, int k) {
        Deque<Integer> dq = new ArrayDeque<>();
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (!dq.isEmpty() && dq.peekFirst() <= i - k) {
                dq.pollFirst();
            }
            while (!dq.isEmpty() && nums[dq.peekLast()] < nums[i]) {
                dq.pollLast();
            }
            dq.addLast(i);
            if (i >= k - 1) {
                res.add(nums[dq.peekFirst()]);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        System.out.println(maxWithinK(nums, k)); // [3, 3, 5, 5, 6, 7]
    }
}
