import java.util.HashSet;
import java.util.Set;
// Given an integer array nums, return 
// true if any value appears at least 
// twice in the array, and return false 
// if every element is distinct.
class ContainsDuplicate{
    public static void main(String[] args){
        Set<Integer> set = new HashSet<Integer>();
        int[] nums = new int[10];
        for(int num: nums){
         if(set.contains(num)){
                System.out.println("FOund");
                return;
            }   
            set.add(num);
        }

        System.out.println("Not found");
    }
}

