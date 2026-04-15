class MinimalSizeSubarraySum {

    public static int minSubArrayLen(int target, int[] nums){
        int left = 0, right = 0;
        int sum = 0;
        int minLength = Integer.MAX_VALUE;
        for(right = 0; right < nums.length; right++){
        sum+= nums[right];
        while(sum >= target){
            minLength = Math.min(minLength, right - left + 1);   
            sum-= nums[left++];
        }
        
        }
        return minLength == Integer.MAX_VALUE ? 0 : minLength;

    }
}