
import java.util.*;

public class AlienDictionary {

    record Pair(char from, char to) {}

    public String findOrder(String[] words) {
        if (words == null || words.length == 0) {
            return "";
        }

        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> indegree = new HashMap<>();

        // Step 1: Register every distinct character.
        for (String word : words) {
            for (char c : word.toCharArray()) {
                graph.putIfAbsent(c, new HashSet<>());
                indegree.putIfAbsent(c, 0);
            }
        }

        // Step 2: Build ordering constraints.
        if (!constructIndegree(words, graph, indegree)) {
            return "";
        }

        // Step 3: Start with characters having no dependencies.
        Queue<Character> queue = new ArrayDeque<>();

        for (char c : indegree.keySet()) {
            if (indegree.get(c) == 0) {
                queue.offer(c);
            }
        }

        // Step 4: Process characters in dependency order.
        StringBuilder result = new StringBuilder();

        while (!queue.isEmpty()) {
            char current = queue.poll();
            result.append(current);

            for (char neighbor : graph.get(current)) {
                int updated = indegree.get(neighbor) - 1;
                indegree.put(neighbor, updated);

                if (updated == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Step 5: Detect cycles.
        if (result.length() != indegree.size()) {
            return "";
        }

        return result.toString();
    }

    private boolean constructIndegree(
            String[] words,
            Map<Character, Set<Character>> graph,
            Map<Character, Integer> indegree) {

        for (int i = 1; i < words.length; i++) {
            String previous = words[i - 1];
            String current = words[i];

            // Invalid prefix ordering.
            if (previous.length() > current.length()
                    && previous.startsWith(current)) {
                return false;
            }

            Pair dependency = findDependency(previous, current);

            if (dependency == null) {
                continue;
            }

            char from = dependency.from();
            char to = dependency.to();

            // Increment indegree only for a new edge.
            if (graph.get(from).add(to)) {
                indegree.put(to, indegree.get(to) + 1);
            }
        }

        return true;
    }

    private Pair findDependency(String first, String second) {
        int minLength = Math.min(first.length(), second.length());

        for (int i = 0; i < minLength; i++) {
            if (first.charAt(i) != second.charAt(i)) {
                return new Pair(first.charAt(i), second.charAt(i));
            }
        }

        return null;
    }

    
}
