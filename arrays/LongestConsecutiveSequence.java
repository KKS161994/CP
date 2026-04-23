
import java.util.*;

class LongestConsecutiveSequence {

    public int maxLength(int[] nums) {
        Set<Integer> numbers = new HashSet<>();
        int maxLength = 0;
        for (int num : nums) {
            numbers.add(num);
        }

        for (int num : numbers) {
            if (!numbers.contains(num - 1)) {
                int current = num;
                int length = 1;

                while (numbers.contains(current+1)) {
                    current++;
                    length++;
                }

                maxLength = Math.max(maxLength, length);
            }
        }

        return maxLength;
    }

}
