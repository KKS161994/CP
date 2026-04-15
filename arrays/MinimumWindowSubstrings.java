/*
 * Question: Minimum Window Substring
 *
 * Given two strings m and n, find the minimum window in m which will contain
 * all the characters of n (including duplicates). If no such window exists,
 * return an empty string.
 *
 * Example:
 *   Input:  m = "ADOBECODEBANC", n = "ABC"
 *   Output: "BANC"
 *
 * Constraints:
 *   - If there is no such window in m that covers all characters of n, return "".
 *   - If there are multiple minimum-length windows, return any one of them.
 */
class MinimumWindowSubstrings{

    public static String minimumWindow(String m, String n){
        int[] letters = new int[26];
        int count = 0;
        int left ,right;
        int[] currentState = new int[26];
        int currentCOunt = 0;
        int lastLeft = 0, lastRight = -1;

        // Traverse through n
        for(left = 0 ; left<n.length();left++){
            letters[n.charAt(left)-'A']++;
            count++;
        }
        
        for(right = 0; right<m.length();right++){

            // Checks if it is part of n
            if(letters[m.charAt(right)-'A']>0){
                //Checks if current state still needs this letter 
                if(letters[m.charAt(right)-'A']>currentState[m.charAt(right)-'A']){
                    currentCOunt++;
                }
                //Increase the letter count in current state
                currentState[m.charAt(right)-'A']++;

                // check if current state satisfies expected state
                while(currentCOunt==count&& left<right){

                    // Update last left and last right if smaller window is found
                    if((right-left+1)<(lastRight-lastLeft+1)){
                        lastLeft = left;
                        lastRight = right;
                    }

                    // See if current letter is part of expected state do some mutations
                    if(letters[m.charAt(left)-'A']>0){
                        if(currentState[m.charAt(left)-'A']==letters[m.charAt(left)-'A']){
                            currentCOunt--;
                        }
                        currentState[m.charAt(left)-'A']--;
                    }
                    left++;
                }
            }
        }

        StringBuilder answer = new StringBuilder();
        for(left = lastLeft; left<=lastRight;left++){
            answer.append(m.charAt(left));
        }
        return answer.toString();
    }

}