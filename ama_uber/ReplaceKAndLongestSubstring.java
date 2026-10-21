// 424. Longest Repeating Character Replacement

// You are given a string s and an integer k. 
// You can choose any character of the string 
// and change it to any other uppercase English 
// character. You can perform this operation at most k times.

// Return the length of the longest substring 
// containing the same letter you can get after
//  performing the above operations.

// ABAAC
// AAAABBC

// BAABAB

// // Length of longest chars togehter 

// BBAAAABCBB

// BBAAABB
import java.util.HashMap;
import java.util.Map;
class ReplaceKAndLongestSubstring{
    public static void main(String[] args){
        String s = "ABABAAA";
        int left = 0;
        int right = 0;
        int maxLen = 0;
        int maxFreq = 0;
        int windowSize = 0;
        int k = 3;
        char leftChar = 'a';
        Map<Character,Integer> freq = new HashMap<>();
        while(right<s.length()){
            char current = s.charAt(right);
            freq.put(
                current,
                freq.getOrDefault(current,0)+1); 
            maxFreq = Math.max(maxFreq,freq.get(current));
            windowSize = right-left+1;
            while(windowSize-maxFreq > k){
                    leftChar = s.charAt(left);
                    int lcFreq = freq.get(leftChar);
                    if(lcFreq>1){
                        freq.put(leftChar,lcFreq-1);
                    }
                    else{
                        freq.remove(leftChar);
                    }
                    left++;
                    windowSize = right - left + 1;

                }
            maxLen = Math.max(maxLen, right - left + 1);

            right++;
        }
        System.out.println(maxLen);


    }

}
