import java.util.List;

import javax.swing.tree.TreeNode;

public class LCA {
    static class Node {
        Node left, right;
        int val;

        Node(int val) {
            this.val = val;
        }

        Node(int val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public boolean findPath(Node root, Node p, List<Node> paths) {
        paths.add(root);
        if (root.equals(p)) {
            return true;
        }
        if (findPath(root.left, target, path)
                || findPath(root.right, target, path)) {
            return true;
        }
        paths.remove(paths.size() - 1);
        return false;

    }

    public Node lowestCommonAncestor(
            Node root,
            Node p,
            Node q) {
        if (root == null)
            return null;
        if (root.val == p.val || root.val == q.val)
            return root;

        Node left = lowestCommonAncestor(root.left, p, q);
        Node right = lowestCommonAncestor(root.right, p, q);

        if ((left != null) && (right != null)) {
            return root;
        }
        return left != null ? left : right;
    }

    class Found {

        boolean foundP, foundQ;

        Found(boolean foundP, boolean foundQ) {
            this.foundP = foundP;
            this.foundQ = foundQ;
        }
    };

    public static Node lowestCommonAncestorv2(
            Node root,
            Node p,
            Node q) {
        Found found = new Found(
                false, false);
        // Your implementation
        Node node = dfs(root, p, q, found);
        if (found.foundP && found.foundQ) {
            return node;
        }
        return null;
    }

    private static Node dfs(
            Node root,
            Node p,
            Node q,
            Found result) {
        if (root == null)
            return null;
        Node left = dfs(root.left, p, q, result);
        Node right = dfs(root.right, p, q, result);
        if (root == p) {
            result.foundP = true;

        }
        if (root == q) {
            result.foundQ = true;

        }

        if (root == p || root == q) {
            return root;
        }
        
        if ((left != null) && (right != null)) {
            return root;
        }
        return left != null ? left : right;
    }
}
