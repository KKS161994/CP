   // if(charToCountMap.size()<=kDistinct){
            //     if(charToCountMap.size()==kDistinct-1&&!charToCountMap.containsKey(currentChar)){
                    
            //     }
            //     else{
            //     charToCountMap.putIfAbsent(currentChar, 0);
            //     charToCountMap.put(currentChar, charToCountMap.get(currentChar)+1);
            //     maxLength = Math.max(maxLength,right-left+1);
            //     continue;
            //     }      
            // }


import java.util.*;


class LongestSubstringWithKDistinctCharacters{
    public static int longestSubstringWithKDistinctCharacters(String input, int kDistinct){
        Map<Character,Integer> charToCountMap = new HashMap();
        int left = 0, maxLength = 0;
        for(int right = 0; right < input.length(); right++){
            char currentChar = input.charAt(right);

            while(charToCountMap.size()==kDistinct && 
            !charToCountMap.containsKey(currentChar)){
                char leftChar = input.charAt(left);
                int count  = charToCountMap.get(leftChar);
                if(count>1){
                    charToCountMap.put(leftChar,count-1);
                }
                else{
                    charToCountMap.remove(leftChar);
                }
                left += 1;
            }
                charToCountMap.putIfAbsent(currentChar, 0);
                charToCountMap.put(currentChar, charToCountMap.get(currentChar)+1);
                maxLength = Math.max(maxLength,right-left+1);
        }
        return maxLength;
    }

    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
            String input = scanner.nextLine();
            int kDistinct = Integer.parseInt(scanner.nextLine());
            System.out.println("Max length is "+
            longestSubstringWithKDistinctCharacters(input,kDistinct));
        }

    }
}