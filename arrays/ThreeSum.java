
import java.util.*;
class ThreeSum{

    public List<List<Integer>> findThreeSum(int[] nums){
    int i, left , right;
    Arrays.sort(nums);
    List<List<Integer>> result = new ArrayList<>();
    for(i = 0; i<nums.length-2;i++){
        left = i +1;
        right = nums.length-1;
        if(i>0 && nums[i]==nums[i-1])
            continue;
        while(left<right){
            int total = nums[left] + nums[right]+nums[i];
            if(total == 0){
                result.add(Arrays.asList(nums[i],nums[left],nums[right]));
                
                while(left<right && nums[left] == nums[left + 1]) left++;
                while(left<right && nums[right] == nums[right - 1]) right--;
                left++;
                right--;
            }
            else if(total>0)
                right--;
            else 
                left++;

        }
    }
    return result;
    }
}