import java.util.ArrayList;
import java.util.List;

class GenValidParanthesis{
   static List<String> answers = new ArrayList<>();
   public static void genParanthesis(int n){
     if(n % 2 != 0 )
     {
        System.out.println("InvaID");
        return;
     }

   genValidParanthesis(n,0,0,new StringBuilder(""));
   }

   public static void genValidParanthesis(int n, int open, int closed, StringBuilder s){
      if(s.length() == n){
         if(open == closed){
            answers.add(s.toString());
         }
         return;
      }
      if(open<n/2){
      
      s.append("(");
      genValidParanthesis(n,open+1,closed,s);
      s.deleteCharAt(s.length() - 1);
      }
      if(open > closed){
      s.append(")");
      genValidParanthesis(n,open,closed+1,s.append(")"));
      s.deleteCharAt(s.length() - 1);
      }
   }     
}