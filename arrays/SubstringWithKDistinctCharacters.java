
import java.util.*;

class SubstringWithKDistinctCharacters{
    public static int maxLength(String input, int kDistinct){
        int left = 0, maxLength = 0;
        Map<Character, Integer> charToCountMap = new HashMap<>();
        for(int right = 0; right<input.length(); right++){
            char currentChar = input.charAt(right);
            charToCountMap.put(currentChar, charToCountMap.getOrDefault(currentChar, 0)+1);
            while(charToCountMap.size()>kDistinct){
                char leftChar = input.charAt(left);
                charToCountMap.put(leftChar,charToCountMap.get(leftChar)-1);
                if(charToCountMap.get(leftChar)==0){
                    charToCountMap.remove(leftChar);
                }
                left+=1;
            }
            maxLength = Math.max(maxLength, right-left+1) ; 
        }
        return maxLength;
    }
    
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
            String input = scanner.nextLine();
            int kDistinct = Integer.parseInt(scanner.nextLine());
            System.out.println("Max length is "+maxLength(input, kDistinct));
        }
    }
}