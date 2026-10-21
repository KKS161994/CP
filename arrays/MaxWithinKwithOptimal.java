
import java.util.ArrayDeque;
import java.util.Deque;


/***
 * Important question 
 * since uses deque to get max within k window in O(n)
 */
class MaxWithinKwithOptimal{
    public int[] maxKwihinOptimal(int[] nums, int K){
        Deque<Integer> dqueue  = new ArrayDeque<>();
        int[] maxWithinKWindow = new int[nums.length-K+1];
        int left = 0;
        for(int right = 0; right<nums.length;right++){
            if(!dqueue.isEmpty() && right>=dqueue.peekFirst()+K){
                dqueue.pollFirst();
            }

            while(!dqueue.isEmpty() && nums[dqueue.peekLast()]<nums[right]){
                dqueue.pollLast();
            }
            dqueue.addLast(right);
            if(right>=K-1){
                maxWithinKWindow[right-K+1] = nums[dqueue.peekFirst()];   
            }
        }
        return maxWithinKWindow;   
    }
}