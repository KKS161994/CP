import java.util.Scanner;

class Permutations {
    private static void printPermutations(String input,char[] output,int left, int right){
        if(left==right){
            System.out.println(output);
            return;
        }

        while(left<=right){
            printPermutations(input, output, left+1, right);
            printPermutations(input, output, left+1, right);
        }
    }

    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
            String input = scanner.nextLine();
            printPermutations(input);
        }
    }
}