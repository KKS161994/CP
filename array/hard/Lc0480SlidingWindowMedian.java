import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * LC 480. Sliding Window Median
 *
 * URL: https://leetcode.com/problems/sliding-window-median/
 * Difficulty: Hard
 * Tags: Array, Hash Table, Sliding Window, Heap (Priority Queue)
 *
 * The median is the middle value in an ordered integer list. If the size of the
 * list is even, there is no middle value. So the median is the mean of the two
 * middle values.
 *
 *     - For examples, if arr = [2,3,4], the median is 3.
 *
 *     - For examples, if arr = [1,2,3,4], the median is (2 + 3) / 2 = 2.5.
 *
 * You are given an integer array nums and an integer k. There is a sliding
 * window of size k which is moving from the very left of the array to the very
 * right. You can only see the k numbers in the window. Each time the sliding
 * window moves right by one position.
 *
 * Return the median array for each window in the original array. Answers within
 * 10^-5 of the actual value will be accepted.
 *
 * Example 1:
 *
 * Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
 * Output: [1.00000,-1.00000,-1.00000,3.00000,5.00000,6.00000]
 * Explanation:
 * Window position                Median
 * ---------------                -----
 * [1  3  -1] -3  5  3  6  7        1
 *  1 [3  -1  -3] 5  3  6  7       -1
 *  1  3 [-1  -3  5] 3  6  7       -1
 *  1  3  -1 [-3  5  3] 6  7        3
 *  1  3  -1  -3 [5  3  6] 7        5
 *  1  3  -1  -3  5 [3  6  7]       6
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,2,3,1,4,2], k = 3
 * Output: [2.00000,3.00000,3.00000,3.00000,2.00000,3.00000,2.00000]
 *
 * Constraints:
 *
 *     - 1 <= k <= nums.length <= 10^5
 *
 *     - -2^31 <= nums[i] <= 2^31 - 1
 *
 */

// NOTE: The Python source body was an empty `pass` stub. Completed minimally
// with the standard two-heap approach (a max-heap for the lower half and a
// min-heap for the upper half) plus lazy deletion to support sliding the window.
public class Lc0480SlidingWindowMedian {

    public double[] medianSlidingWindow(int[] nums, int k) {
        // Max-heap for the smaller half, min-heap for the larger half.
        PriorityQueue<Integer> lo = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Integer> hi = new PriorityQueue<>();
        // Lazy-deletion counts of elements that have left the window.
        Map<Integer, Integer> delayed = new HashMap<>();

        int n = nums.length;
        double[] result = new double[n - k + 1];
        int balance = 0; // (effective size of lo) - (effective size of hi)

        for (int i = 0; i < n; i++) {
            int num = nums[i];

            // Insert num.
            if (lo.isEmpty() || num <= lo.peek()) {
                lo.offer(num);
                balance++;
            } else {
                hi.offer(num);
                balance--;
            }

            // Remove the element leaving the window once the window is full.
            if (i >= k) {
                int out = nums[i - k];
                delayed.merge(out, 1, Integer::sum);
                if (!lo.isEmpty() && out <= lo.peek()) {
                    balance--;
                } else {
                    balance++;
                }
            }

            // Rebalance so that lo has either the same count as hi, or one more.
            if (balance < 0) {
                lo.offer(hi.poll());
                balance += 2;
            } else if (balance > 1) {
                hi.offer(lo.poll());
                balance -= 2;
            }

            prune(lo, delayed);
            prune(hi, delayed);

            if (i >= k - 1) {
                double median;
                if (k % 2 == 1) {
                    median = lo.peek();
                } else {
                    median = ((double) lo.peek() + (double) hi.peek()) / 2.0;
                }
                result[i - k + 1] = median;
            }
        }
        return result;
    }

    // Discard, from the top of the heap, any elements already removed from window.
    private void prune(PriorityQueue<Integer> heap, Map<Integer, Integer> delayed) {
        while (!heap.isEmpty()) {
            int top = heap.peek();
            Integer cnt = delayed.get(top);
            if (cnt == null || cnt == 0) {
                break;
            }
            if (cnt == 1) {
                delayed.remove(top);
            } else {
                delayed.put(top, cnt - 1);
            }
            heap.poll();
        }
    }

    public static void main(String[] args) {
        Lc0480SlidingWindowMedian s = new Lc0480SlidingWindowMedian();
        System.out.println(Arrays.toString(
                s.medianSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)));
        System.out.println(Arrays.toString(
                s.medianSlidingWindow(new int[]{1, 2, 3, 4, 2, 3, 1, 4, 2}, 3)));
    }
}
