
import java.util.*;

class MinimumWindowSubstringWithDuplicates{
    public static String minimumWindowSubString(String m, String n){
        if(m == null || n== null || n.length()>m.length())
            return "Invalid input";

        Map<Character,Integer> substringHashMap = new HashMap<>();
        Map<Character,Integer> mainstringHashMap = new HashMap<>();
        int left,leftIndex=-1,rightIndex = -1, minWindowSize = Integer.MAX_VALUE;
        
        // Convert n to hashmap
        for(left = 0; left< n.length();left++){
            char currentChar = n.charAt(left);
            substringHashMap.put(currentChar, 
            substringHashMap.getOrDefault(currentChar, 0)+1);
        }

        left = 0;
        
        for(int right = 0; right<m.length();right++){
            char currentChar = m.charAt(right);
            char leftChar = m.charAt(left);
            mainstringHashMap.put(currentChar, 
            mainstringHashMap.getOrDefault(currentChar, 0)+1);
            while(mainstringHashMap.entrySet().containsAll(substringHashMap.entrySet())){
                leftIndex = left;
                rightIndex = right;
                minWindowSize = Math.min(minWindowSize, right-left+1);
                mainstringHashMap.put(leftChar,mainstringHashMap.get(leftChar)-1);
                if(mainstringHashMap.get(leftChar)==0){
                    mainstringHashMap.remove(leftChar);
                }
                left+=1;
            }

        }
        
        return m.substring(leftIndex,rightIndex+1);
    }

    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
            String m =  scanner.nextLine();
            String n = scanner.nextLine();
            System.out.println("Minimum window substring is "+minimumWindowSubString(m, n));
        }
    }
}