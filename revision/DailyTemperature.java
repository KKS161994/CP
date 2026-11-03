import java.util.*;

public class DailyTemperature {
    public int[] dailyTemperatures(int[] temperatures) {
        if (temperatures.length == 0)
            return new int[] {};
        if (temperatures.length == 1)
            return new int[] { 0 };
        Stack<Integer> tempStack = new Stack<>();
        int n = temperatures.length;
        int[] result = new int[n];
        int index = 0;
        for (int temp : temperatures) {
            while (!tempStack.isEmpty() && temp > temperatures[tempStack.peek()]) {
                result[tempStack.peek()] = index - tempStack.peek();
                tempStack.pop();
            }
            tempStack.push(index);
            index++;
        }
        while (!tempStack.isEmpty()) {
            result[tempStack.peek()] = 0;
            tempStack.pop();
        }
        // Your implementation
        return result;
    }

    public static void main(String[] args) {
        DailyTemperature solution = new DailyTemperature();

        System.out.println(Arrays.toString(
                solution.dailyTemperatures(
                        new int[] { 73, 74, 75, 71, 69, 72, 76, 73 })));
        // Expected: [1, 1, 4, 2, 1, 1, 0, 0]

        System.out.println(Arrays.toString(
                solution.dailyTemperatures(
                        new int[] { 30, 40, 50, 60 })));
        // Expected: [1, 1, 1, 0]

        System.out.println(Arrays.toString(
                solution.dailyTemperatures(
                        new int[] { 30, 60, 90 })));
        // Expected: [1, 1, 0]

        System.out.println(Arrays.toString(
                solution.dailyTemperatures(
                        new int[] { 90, 80, 70, 60 })));
        // Expected: [0, 0, 0, 0]

        System.out.println(Arrays.toString(
                solution.dailyTemperatures(
                        new int[] { 70, 70, 70 })));
        // Expected: [0, 0, 0]
    }
}
