
/**
Code
Testcase
Test Result
Test Result
474. Ones and Zeroes
Medium
Topics
conpanies icon
Companies
You are given an array of binary strings strs and two integers m and n.

Return the size of the largest subset of strs such that there are at most m 0's and n 1's in the subset.

A set x is a subset of a set y if all elements of x are also elements of y.

 

Example 1:

Input: strs = ["10","0001","111001","1","0"], m = 5, n = 3
Output: 4
Explanation: The largest subset with at most 5 0's and 3 1's is {"10", "0001", "1", "0"}, so the answer is 4.
Other valid but smaller subsets include {"0001", "1"} and {"10", "1", "0"}.
{"111001"} is an invalid subset because it contains 4 1's, greater than the maximum of 3.
Example 2:

Input: strs = ["10","0","1"], m = 1, n = 1
Output: 2
Explanation: The largest subset is {"0", "1"}, so the answer is 2.
**/
class AtMostMZeroesAndNOnes {

    public int computeLargestLength(String[] nums,int m, int n) {
        int[][] numsArray = new int[nums.length][2];
        int i = 0,j=0;
        for(String num : nums){
            numsArray[i++] = countZeroesAndOnes(num);
        }
        // for at most m zero and  ones default value will be zero
        // while for exact m zero and n one default valuw will be Integer.MIN_VALUE
        // since 0 will make a default vlaue which will be true in case of at most
        // but for exact only add when its value greater than 0
        int dp[][] = new int[m+1][n+1];
        dp[0][0] = 0;
        for(int[] num: numsArray){
            int zero = num[0];
            int ones = num[0];
            for(i = m ;i>=zero;i--){
                for(j = n ;j>=ones;j--){

            dp[i][j] = Math.max(dp[i][j] ,dp[i-zero][j-ones]+1);
            }
        }
        }

        return dp[m][n];
    }

    public int[] countZeroesAndOnes(String num) {
        int count0 = 0, count1 = 0;
        for (char letter : num.toCharArray()) {
            count0 += (letter == '0') ? 1 : 0;
            count1 += (letter == '1') ? 1 : 0;
        }
        return new int[]{count0,count1};

    }
}
