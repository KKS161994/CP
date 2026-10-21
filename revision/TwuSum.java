import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

class TwuSum{
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] input = br.readLine().split(" ");
        Set<Integer> set = new HashSet<>();
        int target = Integer.parseInt(br.readLine());
        int[] nums = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            nums[i] = Integer.parseInt(input[i]);
        }
        for(int num: nums){
            if(set.contains(target - num)){
                System.out.println("true");
                return;
            }
            set.add(num);
        }
       System.out.println("false");
    }

}