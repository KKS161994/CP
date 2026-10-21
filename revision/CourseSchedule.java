import java.util.ArrayList;
import java.util.List;

public class CourseSchedule {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // Your implementation

        // 1: construct graph
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        constructGraph(prerequisites, graph);
        // 2: go through each node of graph and dfs and detect
        // if cycle exists
        int[] state = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            if (state[i] != 2 && hasCycle(i, graph, state))
                return false;
        }
        return true;
    }

    private void constructGraph(int[][] prerequisites, List<List<Integer>> graph) {
        int a, b;
        for (int[] item : prerequisites) {
            a = item[0];
            b = item[1];
            graph.get(a).add(b);
        }
    }

    private boolean hasCycle(
            int node,
            List<List<Integer>> graph,
            int[] state) {

        state[node] = 1;

        for (int neighbor : graph.get(node)) {
            // Detect a back edge.
            // Recursively explore unvisited neighbors.
            if (state[neighbor] == 1) {
                return true;
            }
            if (state[neighbor] == 2) {
                continue;
            }
            if (hasCycle(neighbor, graph, state)) {
                return true;
            }
        }

        // Mark this node fully processed.
        state[node] = 2;
        return false;
    }

}
