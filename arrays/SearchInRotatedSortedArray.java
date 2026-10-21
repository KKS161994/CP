
import java.io.*;

class SearchInRotatedSortedArray {

    public int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        if (nums[left] == target) {
            return left;
        }
        if (nums[right] == target) {
            return right;
        }
        while (left <= right) {
            int mid = left + (right - left) / 2;
            // means nums are in sorted order
            if (nums[mid] == target) {
                return mid;
            }
            // CHeck if first half is sorted order;
            if (nums[left] <= nums[mid]) {
                if (target < nums[mid] && target>=nums[left]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if (target <= nums[right] && target>nums[mid]) {
                    left = mid + 1;

                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;

    }
}
