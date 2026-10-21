public class InvertBinaryTree {
    class Node{
        int data;
        Node leftChild,rightChild;
        Node(int data, Node leftChild, Node rightChild){
            this.data = data;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            
        }
    };

    // In place
    public Node invertTree(Node tree){
        if(tree == null) return tree;
        Node tempLeft = invertTree(tree.rightChild);
        Node tempRight = invertTree(tree.leftChild);
        tree.leftChild = tempLeft;
        tree.rightChild = tempRight;

        return tree;
    }

    // Mutating
    public Node invertTreev1( Node tree){
        if(tree == null) return tree;
        return new Node(tree.data,
            invertTreev1(tree.rightChild),
            invertTreev1(tree.leftChild));
    }
}
