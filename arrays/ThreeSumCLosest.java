import java.util.*;

class ThreeSumCLosest {
    public int closestSUm(int[] nums, int target){
        int closestDiff = Integer.MAX_VALUE, sum = Integer.MAX_VALUE,left,right;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length-2; i++) {
            left = i+1;
            right = nums.length - 1;
            while(left<right){
                int currentSum = (nums[i] + nums[left]+nums[right]);
                int difference = target - currentSum;
                if(difference == 0){
                    return target;
                }
                if(Math.abs(difference)<closestDiff){
                        closestDiff = Math.abs(difference);
                        sum = currentSum;
                    }
                if(difference > 0) left++;    
                else right--;
            }

        }
        return sum;
    }
}