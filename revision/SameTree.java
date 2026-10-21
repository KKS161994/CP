// Given the roots of two binary trees p and q, 
// write a function to check if they are the same or not.

// Two binary trees are considered the same if they are structurally identical, 
// and the nodes have the same value.
public class SameTree {
    static class Tree{
        Tree leftNode;
        Tree rightNode;
        int data;
       public Tree(int data,Tree leftNode, Tree rightNode){
            this.data = data;
            this.leftNode = leftNode;
            this.rightNode = rightNode;
        }
    }
    public static void main(String args[]){
        Tree p,q;
        System.out.println(areTreeSame(p,q));

    }

    public static boolean areTreeSame(Tree p, Tree q){
        if(p == null && q== null) return true;
        if(p == null || q== null) return false;
        if(p.data!=q.data) return false;
        return areTreeSame(p.leftNode,q.leftNode) 
        && areTreeSame(p.rightNode,q.rightNode);
    }
}
