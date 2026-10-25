
import java.util.*;

public class PathRouter {
    Node head;
    int order = 1;
    String wildcard = "*";

    class Result {
        int literalCount, order;
        String handler = null;

        Result(int literalCount, int order, String handler) {
            this.literalCount = literalCount;
            this.order = order;
            this.handler = handler;
        }
    }

    class Node {
        String val;
        boolean isTerminal = false;
        int literalCount;
        int order = 0;
        String handler;
        Map<String, Node> paths;

        Node(String val, int literalCount) {
            paths = new HashMap<>();
            this.val = val;
            this.literalCount = literalCount;
        }
    }

    public PathRouter() {
        // Initialize your data structure
        head = new Node("", 0);

    }

    public void addRoute(String path, String handler) {
        if (path.equals("/")) {
            head.isTerminal = true;
            head.handler = handler;
            head.order = order++;
            return;
        }
        path = path.substring(1);
        Node node = head;
        for (String segment : path.split("/")) {
            Node child = node.paths.get(segment);

            if (child == null) {
                int literalCount = node.literalCount
                        + (segment.equals("*") ? 0 : 1);

                child = new Node(segment, literalCount);
                node.paths.put(segment, child);
            }

            node = child;
        }
        node.isTerminal = true;
        node.handler = handler;
        node.order = order++;
    }

    public String getRoute(String path) {
        // Implement
        if (path.equals("/")) {
            return head.isTerminal ? head.handler : null;
        }
        path = path.substring(1);
        String paths[] = path.split("/");
        Result result = new Result(Integer.MIN_VALUE, Integer.MIN_VALUE, null);
        getRoute(head.paths.get("*"), paths, 0, result);
        getRoute(head.paths.get(paths[0]), paths, 0, result);

        return result.handler;
    }

    public void getRoute(Node node, String[] paths, int index, Result result) {
        if (node == null || node.paths == null)
            return;
        if (index == paths.length - 1) {
            if (!node.isTerminal)
                return;
            if (result.literalCount < node.literalCount) {
                result.handler = node.handler;
                result.literalCount = node.literalCount;
                result.order = node.order;
            } else if (result.literalCount == node.literalCount) {
                if (result.order < node.order) {
                    result.handler = node.handler;
                    result.order = node.order;
                }
            }
            return;
        }
        Node wildcardChild = node.paths.get("*");
        Node literalChild = node.paths.get(paths[index + 1]);

        if (wildcardChild != null) {
            getRoute(wildcardChild, paths, index + 1, result);
        }

        if (literalChild != null) {
            getRoute(literalChild, paths, index + 1, result);
        }
    }

    private static void check(
            String name, String actual, String expected) {

        boolean passed = Objects.equals(actual, expected);

        System.out.printf(
                "%s | %s | expected=%s | actual=%s%n",
                passed ? "PASS" : "FAIL",
                name, expected, actual);
    }

    public static void main(String[] args) {
        PathRouter router = new PathRouter();

        router.addRoute("/users/*", "generic");
        router.addRoute("/users/admin", "admin");
        router.addRoute("/users/*/orders", "orders");
        router.addRoute("/users/admin/orders", "adminOrders");

        check("Wildcard", router.getRoute("/users/42"), "generic");
        check("Exact", router.getRoute("/users/admin"), "admin");
        check("Nested wildcard",
                router.getRoute("/users/42/orders"), "orders");
        check("Exact nested",
                router.getRoute("/users/admin/orders"), "adminOrders");
        check("No match",
                router.getRoute("/users/42/settings"), null);

        // Tie-breaking: same number of literal segments
        router.addRoute("/a/*/c", "first");
        router.addRoute("/a/b/*", "second");

        check("Recent registration",
                router.getRoute("/a/b/c"), "second");

        // Re-registering updates priority
        router.addRoute("/a/*/c", "updated");

        check("Re-registration",
                router.getRoute("/a/b/c"), "updated");

        // Wildcard matches exactly one segment
        router.addRoute("/files/*", "file");

        check("One segment",
                router.getRoute("/files/report"), "file");
        check("Too many segments",
                router.getRoute("/files/a/b"), null);
        check("Too few segments",
                router.getRoute("/files"), null);

        // Root route
        router.addRoute("/", "root");
        check("Root", router.getRoute("/"), "root");

    }
}
