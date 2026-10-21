
public class BinaryTreeDiameter {
    public class Node{
        Node leftChild;
        Node rightChild;

        public Node(Node leftChild, Node rightChild){
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            
        }
    }

    static int maxSize = 0;
    public static int getDiameter(Node tree){
        diameter(tree);
        return maxSize;
    }

    public static int diameter(Node tree){
        if(tree == null) return 0;
        int leftSize = diameter(tree.leftChild);
        int rightSize = diameter(tree.rightChild);
        maxSize = Math.max(maxSize,leftSize+rightSize);
        return 1+Math.max(leftSize, rightSize);
    }


    
}
