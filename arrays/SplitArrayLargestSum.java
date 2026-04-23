
class SplitArrayLargestSum {
 public int splitSubarrayInKPartWithMinSum(int[] nums, int K){
    int left = nums[0], right = 0;
    for(int num:nums){
        left = Math.max(left, num);
        right += num;
    }

    int answer = Integer.MAX_VALUE;

    while(left<=right){
        int mid = left + (right-left)/2;
        if(isValid(nums,K, mid)){
            answer = mid;
            right = mid-1;
        }
        else{
            left = mid+1;
        }
    }
    return answer;
 }

public boolean isValid(int[] nums, int K, int W){
    int sum = 0, part = 1;
    
    for(int num:nums){
        if(num>W) return false;

        if(sum+num<=W){
            sum += num;
        }
        else {            
            part++;
            if(part>K) return false;
            sum = num;
        }
    }
    return true;
}

}