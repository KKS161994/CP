import java.util.*;

/**
 * Daily Temperatures (LeetCode 739).
 *
 * Given a list of daily temperatures, return a list such that, for each day,
 * tells how many days you would have to wait until a warmer temperature. If
 * there is no future day for which this is possible, the answer is 0.
 *
 * Uses a monotonic stack of indices.
 *
 * Python original used a list as a stack.
 */
public class WarmTemperatures {

    public static int[] dailyTemp(int[] temps) {
        int[] result = new int[temps.length];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < temps.length; i++) {
            while (!stack.isEmpty() && temps[stack.peek()] < temps[i]) {
                int prevIndex = stack.pop();
                result[prevIndex] = i - prevIndex;
            }
            stack.push(i);
        }

        return result;
    }

    public static void main(String[] args) {
        int[] temps = {73, 74, 75, 71, 69, 72, 76, 73};
        System.out.println(Arrays.toString(dailyTemp(temps)));
        // [1, 1, 4, 2, 1, 1, 0, 0]
    }
}
