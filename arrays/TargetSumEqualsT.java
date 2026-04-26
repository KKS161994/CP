
/***
 * Problem

You are given an array nums and a target T.

You can assign either:

+ or − before each number

Return:

number of ways to assign signs so that expression equals T
 * 
 */
class TargetSumEqualsT {
    public int numberOfWays(int[] nums, int T){
        int sum = 0;
        for(int num: nums){
            sum+=num;
        }

        // If sum is less than T no sign could evalaue to result
        // Say one subset sum is X another is S-X S-X - X = T S-2X = T 
        // So (S-T) /2 =X ie one subset sum should be (S-T)/2 
        // The number of ways we can form this is correct answer
        if(sum < Math.abs(T) || (sum-T)%2!=0) return 0;
        int target = (sum-T)/2;
        int dp[] = new int[target+1];
        dp[0] = 1;

        for(int num:nums){
            for(int i = target; i >= num;i--){
                dp[i]+=dp[i-num];
            }
        }
        return dp[target];
    }
}