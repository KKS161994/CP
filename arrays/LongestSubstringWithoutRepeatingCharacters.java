
import java.util.*;
class LongestSubstringWithoutRepeatingCHaracters{
    private final static HashSet<Character> chars = new HashSet<>();
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        String inputs = sc.next();
        int i = 0, j = 1;
        int maxLength = 0;
        Map<Character, Integer> lastSeenMap = new HashMap<>();
        while(j< inputs.length()){
            if(lastSeenMap.containsKey(inputs.charAt(j))){
                i = Math.max(i, lastSeenMap.get(inputs.charAt(j))+1);
            }
            lastSeenMap.put(inputs.charAt(j), j);
            maxLength = Math.max(maxLength, j-i+1);
            j++;
            // if(!chars.contains(inputs.charAt(j))){
            //     chars.add(inputs.charAt(j));
            //     lastSeenMap.put(inputs.charAt(j), j);
            //     maxLength = Math.max(maxLength, j-i+1);
            //     j++;
            //     continue;
            // }
            // while(chars.contains(inputs.charAt(j))){
            //     chars.remove(inputs.charAt(i));
            //     i++;
            // }
        }
     System.out.println("Max length is "+maxLength);   
    }
}


