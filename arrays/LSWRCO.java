
import java.util.*;

class LongestSubstringWithoutRepeatingCHaracters{

    public static int longestSubString(String input){
        Map<Character,Integer> lastSeen = new HashMap<>();
        int left = 0,maxLength = 0;

        for(int right = 0; right<input.length();right++){
            char currentChar = input.charAt(right);
            if(lastSeen.containsKey(currentChar)){
                left = Math.max(left,lastSeen.get(currentChar)+1);
            }
            lastSeen.put(currentChar,right);
            maxLength = Math.max(maxLength,right-left + 1);
        }

        return maxLength;
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String input = sc.next();
            System.out.println("Max length of non repeating characters"+longestSubString(input));
        }
    }
}