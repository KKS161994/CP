
import java.util.HashSet;
import java.util.Scanner;

class AllPermutations {
     
     private static void permute(String inputString){
        final HashSet<String> permutations = new HashSet<>();
        boolean[] used = new boolean[inputString.length()];
        listPermutations(inputString,  new StringBuilder(), used,permutations);
        for(String item: permutations){
                System.out.println(item);
            }
     }

     private static void listPermutations(String inputString, 
     StringBuilder outputString, boolean[] used, HashSet<String> permutations){

        if(outputString.length() == inputString.length()){
            permutations.add(outputString.toString());
            return;
        }
        for(int i = 0; i< inputString.length();i++){
            if(used[i] == false){
                used[i] = true;
                outputString.append(inputString.charAt(i));
                listPermutations(inputString, outputString,used,permutations);
                outputString.deleteCharAt(outputString.length() - 1);
                used[i] = false;
            }
        }
    }

    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
            String inputString = scanner.nextLine();
            
            permute(inputString);
       
        }
    }
}