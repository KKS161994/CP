
class MaxDepth{
    class TreeNode{
        TreeNode leftChild;
        TreeNode rightChild;

        TreeNode(TreeNode left, TreeNode right){
            this.leftChild = left;
            this.rightChild = right;
        }
    }
    public static void main(String args[]){

    }

    public static int maxDepth(TreeNode node){
        if(node == null)
            return 0;
        return 1 + Math.max(
            maxDepth(node.leftChild),
            maxDepth(node.rightChild)
            );
    }
}