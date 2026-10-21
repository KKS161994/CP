/***
 * 424. Longest Repeating Character Replacement
Medium
Topics
conpanies icon
Companies
You are given a string s and an integer k. 
You can choose any character of the string and 
change it to any other uppercase English character. 
You can perform this operation at most k times.

Return the length of the longest substring containing 
the same letter you can get after performing the above 
operations.

Example 1:

Input: s = "ABAB", k = 2
Output: 4
Explanation: Replace the two 'A's with two 'B's or vice versa.
Example 2:

Input: s = "AABABBA", k = 1
Output: 4
Explanation: Replace the one 'A' in the middle with 'B' and form "AABBBBA".
The substring "BBBB" has the longest repeating letters, which is 4.
There may exists other ways to achieve this answer too.
 

Constraints:

1 <= s.length <= 105
s consists of only uppercase English letters.
0 <= k <= s.length
 */

import java.util.*;
class LongestRepatingCharacterReplacement {
    public static int longestSubstringSize(String s, int k){
        int left = 0, right  = 0;
        Map<Character, Integer> charCountMap = new HashMap<>();
        int maxCount = 0;
        int maxLength = 0;
        for(right = 0; right < s.length(); right++){
            char currentChar = s.charAt(right);
            charCountMap.putIfAbsent(currentChar, 0);   
            charCountMap.put(currentChar, charCountMap.get(currentChar) + 1);
            maxCount = Math.max(maxCount, charCountMap.get(currentChar));
            while(right - left + 1 - maxCount > k){
                char leftChar = s.charAt(left);
                charCountMap.put(leftChar, charCountMap.get(leftChar) - 1);
                if(charCountMap.get(leftChar) == 0){
                    charCountMap.remove(leftChar);
                }
                left++;
                }
            maxLength = Math.max(maxLength, right - left + 1);
    }
return maxLength;
}
public static void main(String[] args) {
    try(Scanner s = new Scanner(System.in)){
        System.out.println("Enter the string: ");
        String str = s.nextLine();
        System.out.println("Enter the value of k: ");
        int k = s.nextInt();
        System.out.println("The length of the longest substring after replacement is: " + longestSubstringSize(str, k));
    }
}

}