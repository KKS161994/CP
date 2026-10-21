import java.util.PriorityQueue;

public class KthLargestElement {
    public static int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for(int num: nums){
            minHeap.offer(num);
            if(minHeap.size()>k){
                minHeap.poll();
            }
        }
        return minHeap.peek();
    }
    public static int findKthLargestv1UsingQuickSelect(int[] nums, int k) {
        int target = nums.length - k;
        int left = 0;
        int right = nums.length - 1;
        int pivotIndex = 0 ;
        while(left<=right){
            pivotIndex = partition(nums,left,right);
            if(pivotIndex == target){
                return nums[target];
            }
            else if(pivotIndex < target){
                left = pivotIndex+1;
            }
            else {
                right = pivotIndex-1;
            }
        }
        return -1; // This line should never be reached if k is valid
    }

    static int partition(int[] nums,int left, int right){
        int i = left;
        int pivot = nums[right];
        for(int j = left;j<right;j++){
            if(nums[j]<=pivot){
                swap(nums,i,j);
                i++;
            }
        }
        swap(nums,i,right);
        return i;
    }

    static void swap(int[] nums,int i, int j){
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }


}
