/***  
 * Longest Substring with At Most Two Distinct Characters

Solution
Given a string s, return the length of the longest substring that contains at most two distinct characters.

 

Example 1:

Input: s = "eceba"
Output: 3
Explanation: The substring is "ece" which its length is 3.
Example 2:

Input: s = "ccaabbb"
Output: 5
Explanation: The substring is "aabbb" which its length is 5.
 

Constraints:

1 <= s.length <= 105
s consists of English letters.
 */

import java.util.*;
class LongestSubstringWithAtMostTwoDistinctCharacter {

    public int longestSubstringTwoDistinct(String s, int k){
        Map<Character, Integer> lastSeenMap = new HashMap<>();
        int count = 0;
        int left = 0;
        for(int right = 0; right < s.length(); right++){
            char currentChar = s.charAt(right);
            lastSeenMap.put(currentChar, right);
            if(lastSeenMap.size() > k){
                int leftmost = Collections.min(lastSeenMap.values());
                lastSeenMap.remove(s.charAt(leftmost));
                left = leftmost + 1;
            }
            count = Math.max(count, right - left + 1);
        }
        return count;
    }

    public static void main(String[] args) {
        try(Scanner s = new Scanner(System.in)){
            System.out.println("Enter the string: ");
            String str = s.nextLine();
            LongestSubstringWithAtMostTwoDistinctCharacter obj = new LongestSubstringWithAtMostTwoDistinctCharacter();
            int result = obj.longestSubstringTwoDistinct(str, 2);
            System.out.println("The length of the longest substring with at most two distinct characters is: " + result);
        }
    }
}