
import java.util.*;

class NextPermutation{
    private static void printNextPermutation(String inputString){
        int right = inputString.length()-1;
        while(right>0 && inputString.charAt(right-1)>inputString.charAt(right)){
            right--;
        }
        
        // Characters are sorted in descending order
        // retunr the fresh order of character
        if(right == 0){
        // return all sorted chars
            char[] sortedChar = inputString.toCharArray();
            Arrays.sort(sortedChar);
            System.out.println(""+new String(sortedChar));
            return;
        }

        int charToReplacePos = right-1;
        int pos = right;
        right = inputString.length()-1;
        int replacedPos = right;

        while(right>=pos){
            replacedPos = inputString.charAt(right)>inputString.charAt(charToReplacePos) 
            && inputString.charAt(right) < inputString.charAt(replacedPos) ? right : replacedPos;

            right--;
        }

        char[] inputArray = inputString.toCharArray();
        char temp = inputArray[charToReplacePos];
        inputArray[charToReplacePos] = inputArray[replacedPos];
        inputArray[replacedPos] = temp;
        inputString = new String(inputArray);

        char[] arr = inputString.toCharArray();
reverse(arr, pos, arr.length - 1);
System.out.println(new String(arr));    

        inputString = sb.toString();
        System.out.println(""+ inputString);
    } 
    
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
            String inputString = scanner.nextLine();
            printNextPermutation(inputString);
        }
    }
}