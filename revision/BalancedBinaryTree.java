
public class BalancedBinaryTree {
    public class Node{
        Node left,right;
        Node(Node left, Node right){
            this.left = left;
            this.right = right;
        }
    }

    public boolean isBalance(Node node){
        if (node == null) return true;
        int leftHeight = height(node.left);
        int rightHeight = height(node.left);
        if(Math.abs(leftHeight-rightHeight)>1) return false;
        return isBalance(node.left) && isBalance(node.right);
    }

    public int height(Node tree){
        if (tree == null) return 0;
        int leftHeight = height(tree.left);
        int rightHeight = height(tree.right);
        return 1+Math.max(leftHeight, rightHeight);
    }    


    public boolean isBalancev1(Node root) {
        // ideally one line
        return heightv1(root) != -1;
    }

    private int heightv1(Node node) {
        // height OR -1
     if(node == null) return 0;
     int leftHeight = heightv1(node.left);
     if(leftHeight == -1) return -1;
     int rightHeight = heightv1(node.right);
     if(rightHeight == -1) return -1;
     if(Math.abs(leftHeight-rightHeight)>1) return -1;
     return Math.max(leftHeight,rightHeight)+1;
    }
}
