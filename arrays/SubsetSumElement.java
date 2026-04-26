import java.io.*;
import java.util.*;

class SubsetSumElement{
    public static int minDiff(int[] nums) {

        int sum = 0;
        for (int num : nums) {
            sum += num;
        }

        int half_sum = sum / 2;

        boolean[] dp = new boolean[half_sum + 1];
        dp[0] = true;

        for (int num : nums) {
            for (int i = half_sum; i >= num; i--) {
                dp[i] = dp[i] || dp[i - num];
            }
        }

        for (int i = half_sum; i >= 0; i--) {
            if (dp[i]) {
                return sum - 2 * i;
            }
        }

        return 0;
    }
}