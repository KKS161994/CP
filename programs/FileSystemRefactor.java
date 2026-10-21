import java.util.*;

/**
 * In-memory file system (refactored version) backed by a trie of Nodes.
 *
 * Supports:
 *   - ls(path): list directory contents (sorted) or the file name itself
 *   - mkdir(path): create directories along the path
 *   - addContentToFile(filePath, content): append content, marking node as a file
 *   - readContentFromFile(filePath): read a file's content
 *
 * A path "/" denotes the root; otherwise paths are split on "/".
 */
public class FileSystemRefactor {

    static class Node {
        String content = "";
        Map<String, Node> children = new HashMap<>();
        boolean isFile = false;
    }

    private final Node root;

    public FileSystemRefactor() {
        this.root = new Node();
    }

    private List<String> getParts(String path) {
        if (path.equals("/")) {
            return new ArrayList<>();
        }
        String[] parts = path.split("/");
        // path.split("/")[1:] in Python: drop the leading empty element
        List<String> result = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            result.add(parts[i]);
        }
        return result;
    }

    private Node traverse(String path, boolean createMissing) {
        Node node = root;
        for (String part : getParts(path)) {
            if (!node.children.containsKey(part)) {
                if (!createMissing) {
                    throw new IllegalArgumentException("Path does not exist");
                }
                node.children.put(part, new Node());
            }
            node = node.children.get(part);
        }
        return node;
    }

    public List<String> ls(String path) {
        Node node = traverse(path, false);
        if (node.isFile) {
            List<String> parts = getParts(path);
            List<String> result = new ArrayList<>();
            result.add(parts.get(parts.size() - 1));
            return result;
        }
        List<String> keys = new ArrayList<>(node.children.keySet());
        Collections.sort(keys);
        return keys;
    }

    public void mkdir(String path) {
        traverse(path, true);
    }

    public void addContentToFile(String filePath, String content) {
        Node node = traverse(filePath, true);
        node.isFile = true;
        node.content += content;
    }

    public String readContentFromFile(String filePath) {
        Node node = traverse(filePath, false);
        return node.content;
    }
}
