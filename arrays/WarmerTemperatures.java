import java.util.Stack;


class WarmerTemperatures{
    public int[] warmerDays(int[] temps){
        Stack<Integer> tempStack = new Stack<>();
        int[] results = new int[temps.length];
        // Stack will contain only temperatures which have no warmer days and have been process till ie 
        // it will be in decreasing order

        for(int i = 0; i< temps.length;i++){
            while(!tempStack.isEmpty() && temps[tempStack.peek()]<temps[i])
                {   
                    // getting peek index is better than everytime using peek in place e
                    // else it has high chance of being mutated
                    int peekIndex = tempStack.peek();
                    results[peekIndex] = i-peekIndex;
                    tempStack.pop();
                }
                tempStack.push(i);
        }
        return results;
    }
}