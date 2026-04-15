
import java.util.*;
class LongestSubstringWithoutRepeatingCHaracters{
    private final static HashSet<Character> chars = new HashSet<>();
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        String inputs = sc.next();
        int i = 0, j = 1;
        int maxLength = 0;
        while(i < j && j< inputs.length()){
            if(!chars.contains(inputs.charAt(j))){
                chars.add(inputs.charAt(j));
                maxLength = Math.max(maxLength, j-i+1);
                j++;
                continue;
            }
            while(chars.contains(inputs.charAt(j))){
                chars.remove(inputs.charAt(i));
                i++;
            }
        }
     System.out.println("Max length is "+maxLength);   
    }
}


