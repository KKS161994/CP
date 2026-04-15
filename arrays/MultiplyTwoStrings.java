import java.util.*;
class MultiplyTwoStrings {

    private static void multiplyTwoStrings(String m, String n){
        int idx ;
        ArrayList<Integer> answers = new ArrayList<>();
        StringBuilder digit1 = new StringBuilder(m.length()>=n.length()?m:n);
        StringBuilder digit2 = new StringBuilder(m.length()<n.length()?m:n);
        digit1 = digit1.reverse();
        digit2 = digit2.reverse();
        String dig1 = new String(digit1);
        String dig2 = new String(digit2);
        for(idx = 0 ; idx < dig1.length() + dig2.length() ; idx++){
            answers.add(0);
        }

         for(idx = 0; idx < dig2.length(); idx++){
            // multiply
            multiplyWholeStringBySingleDigitAndAddToResult(dig1,dig2.charAt(idx),answers,idx);
         }

        idx = ((dig1.length()+dig2.length())-1);
        while(answers.get(idx)==0){
            idx--;
        }
        if(idx==-1)
        {
            System.out.println("0");
            return;
        }
        
        StringBuilder finalAnswer = new StringBuilder();
        while(idx>=0){
            finalAnswer.append(answers.get(idx--));
        }
        System.out.println(new String(finalAnswer));
    }

    private static void multiplyWholeStringBySingleDigitAndAddToResult(String m, Character n, ArrayList<Integer> answers, int idx){
        int carry = 0,ctr =0;
        int number = n-'0';
        for(ctr = 0 ; ctr < m.length();ctr++){
            int sum = (m.charAt(ctr)-'0')*number+carry+answers.get(idx+ctr);
            carry = sum/10;

            answers.set(idx+ctr, sum%10);
        }
        while(carry>0){
            answers.set(idx+(ctr++), carry%10);
            carry/=10;
        }

    }

    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
            String m = scanner.nextLine();
            String n = scanner.nextLine();
            multiplyTwoStrings(m, n);
        }
    }
}