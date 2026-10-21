
import java.util.*;

class MaxWithinK {

    public int[] maxWithinK(int[] nums, int K) {
        int left = 0, right;
        
        PriorityQueue<Integer> valueHeap = new PriorityQueue<>(Collections.reverseOrder());
        Map<Integer, Integer> currentState = new HashMap<>();

        int[] result = new int[nums.length-K+1];
        
        for (right = 0; right < nums.length; right++) {
            // Add element to heap and state
            currentState.put(nums[right], currentState.getOrDefault(nums[right], 0) + 1);
            valueHeap.add(nums[right]);

            // If current state exceeds K remove left element
            if (right - left + 1 > K) {
                currentState.put(nums[left], currentState.get(nums[left]) - 1);
                left++;
            }

            while (currentState.get(valueHeap.peek()) == 0) {
                valueHeap.poll();            
            }
            
            // If window size is K
            if(right-left+1==K) result[left] = valueHeap.peek();
        }
        return result;
    }
}
