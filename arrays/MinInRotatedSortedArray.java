/***
 * 
 * Right comparision is suitable one because left breaks when mid+1 
 * is pivot element
 * for left breaks when duplicate even few hence if else if and then else
***/
class MinInRotatedSortedArray{
    public int minInRotateSortedArray(int[] nums){
        int left = 0, right = nums.length-1,min=nums[0];
        while(left<=right){
            int mid = left+(right-left)/2;
            min = Math.min(nums[mid],min);
            if(nums[mid]<nums[right]){
                right = mid;
            }
            else if(nums[mid]>nums[right]){
                left = mid+1;
            } else{
                right --;
            }
        }
        return min;
    }
}
/***
 *  if(nums[mid]<nums[right]){
                right = mid;
            }
            else if(nums[mid]>right){
                left = mid +1;
            }
            else {
                right--;
            }

 */

/***
 * 4 5 6 7 2 3
 * 
 * 
 * 5 6 4 5  5 5 
 * 
 * 4 5 6 1 2 3
 */