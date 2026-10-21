import java.util.Collection;
import java.util.Comparator;
import java.util.ArrayDeque;
import java.util.Set;

class LongestCOntinuosSubarray {
  

    class Node {
        int value, index;

        Node(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }

    public int longestSubarray(int[] nums, int limit) {
        if (nums == null || nums.length == 0)
            return 0;
        PriorityQueue<Node> minQueue = new PriorityQueue<>(
            (Node o1, Node o2) -> o1.value - o2.value
        );
        PriorityQueue<Node> maxQueue = new PriorityQueue<>(
                (Node o1, Node o2) -> o2.value - o1.value);
        Set<Integer> removedELements = new HashSet<>();
        int left = 0, right = 0, maxSize = 1, min = nums[0], max = nums[0];
        minQueue.add(new Node(nums[0],0));
        maxQueue.add(new Node(nums[0],0));
        while (right < nums.length - 1) {
            right++;
            minQueue.add(new Node(nums[right],right));
            maxQueue.add(new Node(nums[right],right));

            while (removedELements.contains(minQueue.peek().index)) {
                minQueue.poll();
            }
            while (removedELements.contains(maxQueue.peek().index)) {
                maxQueue.poll();
            }
            min = minQueue.peek().value;
            max = maxQueue.peek().value;
            while (max - min > limit) {
                removedELements.add(left);
                left++;
                while (removedELements.contains(minQueue.peek().index)) {
                    minQueue.poll();
                }
                min = minQueue.peek().value;
                while (removedELements.contains(maxQueue.peek().index)) {
                    maxQueue.poll();
                }
                max = maxQueue.peek().value;
            }
            maxSize = Math.max(maxSize, right-left+1);

        }

        return maxSize;
    }

    public int longestSubarrayV1(int[] nums, int limit) {
        if (nums == null || nums.length == 0)
            return 0;
        ArrayDeque<Integer> minQueue = new ArrayDeque<>();
        ArrayDeque<Integer> maxQueue = new ArrayDeque<>();
        int left = 0, right = 0, maxSize = 1;
        minQueue.add(0);
        maxQueue.add(0);
        while (right < nums.length - 1) {
            right++;
            

            while (!minQueue.isEmpty()&&nums[minQueue.peekLast()]>nums[right]) {
                minQueue.pollLast();
            }
            while (!maxQueue.isEmpty()&&nums[maxQueue.peekLast()]<nums[right]) {
                maxQueue.pollLast();
            }
            minQueue.add(right);
            maxQueue.add(right);
            
            while (nums[maxQueue.getFirst()] - nums[minQueue.getFirst()] > limit) {
                left++;
                while (minQueue.getFirst()<left) {
                    minQueue.pollFirst();
                }
                while (maxQueue.getFirst()<left) {
                    maxQueue.pollFirst();
                }
            }
            maxSize = Math.max(maxSize, right-left+1);

        }

        return maxSize;
    }
}