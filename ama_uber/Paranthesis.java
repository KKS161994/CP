import java.util.Stack;

class Paranthesis{

    public static void main(String[] args){
        String s = "{}{}()[]";
        Stack<Character> stack = new Stack<>();
        for(char c : s.toCharArray()){
            if (c == '{' || c == '[' || c == '(') {
                stack.push(c);
            } else {
            if(!stack.isEmpty()){
                switch(c){
                    case '}' -> {
                        if(stack.peek().equals('{')){
                            stack.pop();               
                        }else {
                            System.out.println("Invalid");
                            return;
                        }
                        
                    }
                    case ']' -> {
                        if(stack.peek().equals('[')){
                            stack.pop();               
                        }else {
                            System.out.println("Invalid");
                            return;
                        }
                    }
                    case ')' -> {
                        if(stack.peek().equals('(')){
                            stack.pop();               
                        }else {
                            System.out.println("Invalid");
                            return;
                        }
                    }
                }
            }
        }
        }
        if(stack.isEmpty()){
            System.out.println("Valid");
            return;
        }
        System.out.println("Invalid");
    }
}