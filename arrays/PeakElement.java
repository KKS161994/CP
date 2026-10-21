
/**
 * 17:43Claude responded: LC 162 — Find Peak ElementLC 162 — Find Peak Element
 * Array nums, find any element strictly greater than its neighbors. nums[-1] and nums[n] are treated as -∞.
 * Must be O(log n).
 * [1, 2, 3, 1] → 2
 * [1, 2, 1, 3, 5, 6, 4] → 5 (or 1)
 * Go ahead.
 */


class PeakElement {

    public int findPeak(int[] nums) {

        int left = 0, right = nums.length - 1, mid = -1;
        if (nums.length == 0) {
            return -1;
        }
        if (nums.length == 1) {
            return 0;
        }
        while (left < right) {
            mid = left + (right-left)/2;
            if(nums[mid]<nums[mid+1])
                left = mid+1;
            else right = mid;
        }
        return mid;
    }
}
