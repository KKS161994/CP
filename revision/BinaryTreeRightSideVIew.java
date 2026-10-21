import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinaryTreeRightSideVIew {
    static class Node{
        Node left,right;
        int val;

        Node(int val){
            this.val = val;
        }
        Node(int val,Node left, Node right){
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    public static List<Integer> btRightView(Node root){
        if(root == null) return null;
        List<Integer> result = new ArrayList<>();
        Map<Integer, Integer> levelToNode = new HashMap();

        levelToNode.put(0,root.val);
        btRightView(root,0,levelToNode);
        for(int index = 0; index <= levelToNode.size(); index++){
            result.add(levelToNode.get(index));
        }
        return result;
    }

    public static void btRightView(Node root, int level, Map<Integer, Integer> levelToNod){
        if(root == null) return;
        if(!levelToNod.containsKey(level)){
            levelToNod.put(level, root.val);
        }
        btRightView(root.right,level+1,levelToNod);
        btRightView(root.left,level+1,levelToNod);
    }

}
