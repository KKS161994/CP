// // 560. Subarray Sum Equals K
// Medium
// Topics
// Hint
// Given an array of integers nums and an integer k, return the total number of subarrays whose sum equals to k.

// A subarray is a contiguous non-empty sequence of elements within an array.

 

// Example 1:

// Input: nums = [1,1,1], k = 2
// Output: 2
// Example 2:

// Input: nums = [1,2,3], k = 3
// Output: 2
import java.util.HashMap;
import java.util.Map;
public class SubarraySum {
    public static void main(String args[]){
        Map<Integer,Integer> sumToIndex = new HashMap<>();
        int target = 10;
        int[] nums = new int[5];
        int prevSum  = 0,count = 0,leftSum,newSum;
        for(int num: nums){
            
            newSum = prevSum+num;
            leftSum = newSum-target;
            if(sumToIndex.containsKey(leftSum)){
                count+=sumToIndex.get(leftSum);
            }
            
            sumToIndex.put(newSum,sumToIndex.getOrDefault(newSum,0)+1);
        }
        System.out.println("The sum count is "+count);
    }
}
