// Question 1 — Path Router with Wildcards
// Atlassian-style mock
// Design and implement an in-memory URL path router.

// The router supports registering a path pattern with a result and looking up the result associated with a requested path.

// A registered path may contain a wildcard segment *, which matches exactly one non-empty path segment.
// Required operations
// class Router {
//     public void addRoute(String path, String result);

//     public String callRoute(String path);
// }

// Example
// Router router = new Router();

// router.addRoute("/users/profile", "profile");
// router.addRoute("/users/*/settings", "settings");
// router.addRoute("/products/*", "product");

// router.callRoute("/users/profile");       // "profile"
// router.callRoute("/users/123/settings");  // "settings"
// router.callRoute("/products/42");         // "product"
// router.callRoute("/products/42/reviews"); // null

// Test cases
// 1: // router.addRoute("/users/profile", "profile");
// // router.addRoute("/users/*/settings", "settings");
// router.callRoute("/users/profile");   answer profile
// 
// 
// 
// 2: router.addRoute("/users/*/profile", "profiles");
//    router.addRoute("/*/me/profile", "profile");  
//   router.callRoute("/users/me/profile");   answer profile
// 
// 
// 
// 
// 
// 
// 
// 
// 
// 
// 

import java.util.HashMap;
import java.util.Map;

public class PathRouterWithWildcards {
    private final String wildcard = "*";

    public class TrieNode {
        String segment;
        boolean isTerminal = false;
        int order;
        Map<String, TrieNode> nodes;
        String value;
        int literalCount = 0;

        TrieNode(String segment, boolean isTerminal, Map<String, TrieNode> nodes) {
            this.segment = segment;
            this.isTerminal = isTerminal;
            this.nodes = nodes;
        }

    }

    private TrieNode root;
    int count = 0;

    PathRouterWithWildcards() {
        root = new TrieNode("root", false, new HashMap<>());
    }

public void addRoute(String path, String result) {
    String[] segments = path.substring(1).split("/");
    TrieNode current = root;

    for (String segment : segments) {
        TrieNode child = current.nodes.get(segment);

        if (child == null) {
            child = new TrieNode(segment, false, new HashMap<>());
            child.literalCount = current.literalCount
                    + (segment.equals("*") ? 0 : 1);

            current.nodes.put(segment, child);
        }

        current = child;
    }

    current.isTerminal = true;
    current.value = result;
    current.order = count++;
}



    public String callRoute(String path) {
        String[] routes = path.substring(1).split("/");
        TrieNode result = new TrieNode(path, false, null);
        result.value = null;
        result.order = -1;
        result.literalCount = -1;

        callRoute(root, routes, 0, result);
        return result.value;
    }

    private void callRoute(TrieNode node, String[] segments, int index, TrieNode result) {
        if (node == null)
            return;
        if (index == segments.length) {

            if (!node.isTerminal) {
                return;
            }

            if (result.value == null
                    || node.literalCount > result.literalCount
                    || (node.literalCount == result.literalCount
                            && node.order > result.order)) {

                result.value = node.value;
                result.literalCount = node.literalCount;
                result.order = node.order;
            }

            return;
        }
        // "a aa bb aa bc a" k = 2 aa a
        TrieNode exact = node.nodes.get(segments[index]);

        if (exact != null) {
            callRoute(exact, segments, index + 1, result);
        }

        // Try wildcard branch.
        TrieNode wildcardNode = node.nodes.get(wildcard);

        if (wildcardNode != null) {
            callRoute(wildcardNode, segments, index + 1, result);
        }
    }

    public static void main(String[] args) {

        PathRouterWithWildcards router = new PathRouterWithWildcards();

        router.addRoute("/users/profile", "user-profile");
        router.addRoute("/users/*/settings", "settings");
        router.addRoute("/products/*", "product");

        test(router, "/users/profile", "user-profile");
        test(router, "/users/123/settings", "settings");
        test(router, "/products/42", "product");
        test(router, "/products/42/reviews", null);

        router.addRoute("/users/*/profile", "profiles");
        router.addRoute("/*/me/profile", "profile");

        test(router, "/users/me/profile", "profile");

        router.addRoute("/users/admin/profile", "admin-profile");

        test(router, "/users/admin/profile", "admin-profile");

        router.addRoute("/users/admin/profile", "updated-profile");

        test(router, "/users/admin/profile", "updated-profile");
    }

    private static void test(
            PathRouterWithWildcards router,
            String path,
            String expected) {

        String actual = router.callRoute(path);

        boolean passed = java.util.Objects.equals(actual, expected);

        System.out.printf(
                "%s | path=%s | expected=%s | actual=%s%n",
                passed ? "PASS" : "FAIL",
                path,
                expected,
                actual);
    }
}
