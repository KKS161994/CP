import java.util.*;
// prefixSum.put(0,1);     
// Very important handles case where initial element is part of target
class CountSubarrays{
    public int subarraysCount(int[] nums,int target){
        Map<Integer,Integer> prefixSum = new HashMap<>();
        int count=0,sum = 0;
        prefixSum.put(0,1);     // Very important handles case where initial element is part of target
        for(int num: nums){
            sum+=num;
            int diff = sum-target;
            if(prefixSum.containsKey(diff)) count += prefixSum.get(diff);
            prefixSum.putIfAbsent(sum, 0);
            prefixSum.put(sum, prefixSum.get(sum)+1);
            
        }
        return count;
    }
}