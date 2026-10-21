import java.util.ArrayDeque;
import java.util.Deque;
class SameTree{

class TreeNode {
    TreeNode left,right;
    int data;

    TreeNode(TreeNode left, TreeNode right,int data) {
        this.left = left;
        this.right = right;
        
    }
}

    
    public static boolean sameTree(TreeNode tree1, TreeNode tree2){
        if(tree1 == null && tree2 == null){
            return true;
        }
        if(tree1 == null || tree2 == null){
            return false;
        }

        if(tree1.data != tree2.data) return false;
        return sameTree(tree1.left,tree2.left) && sameTree(tree1.right,tree2.right);
    }


    public static class NodePair{
        TreeNode node1, node2;
        NodePair(TreeNode node1,TreeNode  node2){
            this.node1 = node1;
            this.node2 = node2;
        }
    }

    public static boolean sameTreev1(TreeNode v1, TreeNode v2){
        Deque<NodePair> stack = new ArrayDeque<>();
        stack.push(new NodePair(v1, v2));

        while(!stack.isEmpty()){
            NodePair np = stack.pop();
            TreeNode temp1 = np.node1;
            TreeNode temp2 = np.node2;
        if (temp1 == null && temp2 == null)
            continue;

        if (temp1 == null || temp2 == null)
            return false;

            if(temp1.data != temp2.data){
                return false;
            }
            stack.push(new NodePair(temp1.left,temp2.left));
            stack.push(new NodePair(temp1.right,temp2.right));
        }
        return true;
    }
}