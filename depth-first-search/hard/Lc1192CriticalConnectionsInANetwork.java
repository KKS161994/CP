import java.util.*;

/**
 * LC 1192. Critical Connections in a Network
 *
 * URL: https://leetcode.com/problems/critical-connections-in-a-network/
 * Difficulty: Hard
 * Tags: Depth-First Search, Graph Theory, Biconnected Component
 *
 * There are n servers numbered from 0 to n - 1 connected by undirected
 * server-to-server connections forming a network where connections[i] = [ai, bi]
 * represents a connection between servers ai and bi. Any server can reach other
 * servers directly or indirectly through the network.
 *
 * A critical connection is a connection that, if removed, will make some servers
 * unable to reach some other server.
 *
 * Return all critical connections in the network in any order.
 *
 * Example 1:
 *
 * Input: n = 4, connections = [[0,1],[1,2],[2,0],[1,3]]
 * Output: [[1,3]]
 * Explanation: [[3,1]] is also accepted.
 *
 * Example 2:
 *
 * Input: n = 2, connections = [[0,1]]
 * Output: [[0,1]]
 *
 * Constraints:
 *
 *     - 2 <= n <= 10^5
 *
 *     - n - 1 <= connections.length <= 10^5
 *
 *     - 0 <= ai, bi <= n - 1
 *
 *     - ai != bi
 *
 *     - There are no repeated connections.
 *
 */
public class Lc1192CriticalConnectionsInANetwork {

    private Map<Integer, List<Integer>> graph;
    private int[] discovery;
    private int[] low;
    private List<List<Integer>> answer;
    private int time;

    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        graph = new HashMap<>();
        for (List<Integer> conn : connections) {
            int u = conn.get(0), v = conn.get(1);
            graph.computeIfAbsent(u, key -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, key -> new ArrayList<>()).add(u);
        }
        discovery = new int[n];
        low = new int[n];
        Arrays.fill(discovery, -1);
        Arrays.fill(low, -1);
        answer = new ArrayList<>();
        time = 0;

        dfs(0, -1);
        return answer;
    }

    private void dfs(int node, int parent) {
        discovery[node] = time;
        low[node] = time;
        time += 1;
        for (int neighbour : graph.getOrDefault(node, Collections.emptyList())) {
            if (neighbour == parent) {
                continue;
            }
            if (discovery[neighbour] == -1) {
                dfs(neighbour, node);
                // Here we consider low of neighbour not discovery because
                // discovery will surely be larger than current.
                low[node] = Math.min(low[node], low[neighbour]);
                if (low[neighbour] > discovery[node]) {
                    answer.add(new ArrayList<>(Arrays.asList(node, neighbour)));
                }
            } else {
                // This goes to discovery because the discovered edge is visited
                // and it means it was already seen; looking out for discovered
                // is a better correctness approach. Lowest for current node
                // should be discovery for neighbour which was already visited.
                low[node] = Math.min(low[node], discovery[neighbour]);
            }
        }
    }

    public static void main(String[] args) {
        Lc1192CriticalConnectionsInANetwork s = new Lc1192CriticalConnectionsInANetwork();
        System.out.println(s.criticalConnections(4, Arrays.asList(
                Arrays.asList(0, 1), Arrays.asList(1, 2),
                Arrays.asList(2, 0), Arrays.asList(1, 3))));
        System.out.println(s.criticalConnections(2, Arrays.asList(
                Arrays.asList(0, 1))));
    }
}
