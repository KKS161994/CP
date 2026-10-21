import java.util.HashMap;
import java.util.Map;

class LongestSubstringWithoutRepeatingChar{
    public static void main(String args[]){
        String s = "hello how are you";
        Map<Character, Integer> lastIndexes = new HashMap<>();
        int i = 0, j = 0, n = s.length(),len = 0;
        while(j<n){
         char current = s.charAt(j);
         if(!lastIndexes.containsKey(current) || lastIndexes.get(current)<i){
            lastIndexes.put(current,j);
            len = Math.max(len, j- i +1);
         }
         else{
            i = lastIndexes.get(current)+1;
            lastIndexes.put(current,j);
         }
         j++;   
        }
    }
}