import java.util.ArrayDeque;
public class SameTreeOptimised {
    record Tree(int data,Tree leftChild,Tree rightChild){}
    record NodePair(Tree p, Tree q){}
    public static void main(String args[]){
        Tree p,q;

        
    }
    
    public static boolean isSameTree(Tree p , Tree q){
        ArrayDeque<NodePair> stack = new ArrayDeque<>();
        stack.push(new NodePair(p,q));
        while(!stack.isEmpty()){
            NodePair pair = stack.pop();
            Tree m = pair.p;
            Tree n = pair.q;
            if(m == null && n == null) continue;
            if(m == null || n == null) return false;
            if(m.data != n.data) return false;
            stack.push(new NodePair(m.leftChild,n.leftChild));
            stack.push(new NodePair(m.rightChild,n.rightChild));
        }
        return true;
    }
}
