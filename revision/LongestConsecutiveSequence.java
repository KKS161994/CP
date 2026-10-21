import java.util.HashSet;
import java.util.Set;
public class LongestConsecutiveSequence {
public static void main(String args[]){
    int nums[] = new int[10];
    Set<Integer> set = new HashSet<>();
    int maxSize = 1;

    // store element in set
    for(int num: nums){
        set.add(num);
    }

    // start iterating through set and check if 
    // -1 exist ignore it else and iterate at +1 in set and 
    // keep updating maxSize if possible
    for(int num:nums){
        if(set.contains(num-1)) continue;
        int temp = num,tempSize=1;
        while(set.contains(temp+1)){
            temp+=1;
            tempSize+=1;
        }
        maxSize = Math.max(maxSize, tempSize);
    }

    System.out.println("Max size is "+maxSize);


}    
}
