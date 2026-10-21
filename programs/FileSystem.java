import java.util.*;

/**
 * In-memory file system backed by a trie of Nodes (LeetCode "Design In-Memory
 * File System" style).
 *
 * Supports:
 *   - ls(filepath): list directory contents (sorted) or the file name itself
 *   - mkdir(filePath): create directories along the path
 *   - addContentToFile(filePath, content): append content, marking node as eof (a file)
 *   - readContentFromFile(filePath): read a file's content, or "" if not a file
 *
 * Your FileSystem object will be instantiated and called as such:
 *   FileSystem obj = new FileSystem();
 *   List<String> param_1 = obj.ls(path);
 *   obj.mkdir(path);
 *   obj.addContentToFile(filePath, content);
 *   String param_4 = obj.readContentFromFile(filePath);
 */
public class FileSystem {

    static class Node {
        String content = "";
        // mirrors Python defaultdict(Node); we create children explicitly below
        Map<String, Node> nodes = new HashMap<>();
        boolean isEof = false; // eof describes that it's a file and an end
    }

    private final Node root;

    public FileSystem() {
        this.root = new Node();
    }

    private List<String> getParts(String filepath) {
        List<String> result = new ArrayList<>();
        if (!filepath.equals("/")) {
            String[] parts = filepath.split("/");
            for (int i = 1; i < parts.length; i++) {
                result.add(parts[i]);
            }
        }
        return result;
    }

    public List<String> ls(String filepath) {
        List<String> paths = getParts(filepath);
        Node node = root;
        for (String path : paths) {
            if (!node.nodes.containsKey(path)) {
                break;
            }
            node = node.nodes.get(path);
        }
        if (node.isEof) {
            List<String> result = new ArrayList<>();
            result.add(paths.get(paths.size() - 1));
            return result;
        }
        List<String> keys = new ArrayList<>(node.nodes.keySet());
        Collections.sort(keys);
        return keys;
    }

    public void mkdir(String filePath) {
        List<String> paths = getParts(filePath);
        Node node = root;
        for (String path : paths) {
            if (!node.nodes.containsKey(path)) {
                Node newNode = new Node();
                node.nodes.put(path, newNode);
                node = newNode;
            } else {
                node = node.nodes.get(path);
            }
        }
    }

    public void addContentToFile(String filePath, String content) {
        List<String> paths = getParts(filePath);
        Node node = root;
        for (String path : paths) {
            if (!node.nodes.containsKey(path)) {
                Node newNode = new Node();
                node.nodes.put(path, newNode);
                node = newNode;
            } else {
                node = node.nodes.get(path);
            }
        }
        node.content += content;
        node.isEof = true;
    }

    public String readContentFromFile(String filePath) {
        List<String> paths = getParts(filePath);
        Node node = root;
        for (String path : paths) {
            // Assuming the path always exists else break
            if (!node.nodes.containsKey(path)) {
                break;
            }
            node = node.nodes.get(path);
        }
        if (node.isEof) {
            return node.content;
        }
        return "";
    }
}
