import java.util.*;

/**
 * Floyd-Warshall: all-pairs shortest paths.
 *
 * Given a weighted graph (edges may be negative, but no negative cycles),
 * compute the shortest distance between every pair of vertices.
 *
 * Time:  O(V^3)
 * Space: O(V^2)
 */
public class FloydWarshall {

    static final double INF = Double.POSITIVE_INFINITY;

    /**
     * n     : number of vertices (labelled 0 .. n-1)
     * edges : list of (u, v, w) directed edges with weight w
     * returns dist[i][j] = shortest distance from i to j (INF if unreachable)
     */
    public static double[][] floydWarshall(int n, int[][] edges) {
        // dist[i][j] starts as INF, except dist[i][i] = 0
        double[][] dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], INF);
        }
        for (int i = 0; i < n; i++) {
            dist[i][i] = 0;
        }

        for (int[] e : edges) {
            int u = e[0], v = e[1], w = e[2];
            dist[u][v] = Math.min(dist[u][v], w); // min handles parallel edges
        }

        // k = intermediate vertex allowed on the path
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (dist[i][k] == INF) {
                    continue; // skip: no path i -> k, so no improvement via k
                }
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        //     (0) --4--> (1)
        //      |          |
        //      1          2
        //      v          v
        //     (2) <--3-- (3)
        int n = 4;
        int[][] edges = {
            {0, 1, 4},
            {0, 2, 1},
            {1, 3, 2},
            {3, 2, 3},
        };
        double[][] dist = floydWarshall(n, edges);
        for (double[] row : dist) {
            List<String> out = new ArrayList<>();
            for (double d : row) {
                if (d == INF) {
                    out.add("INF");
                } else {
                    out.add(String.valueOf((long) d));
                }
            }
            System.out.println(out);
        }
    }
}
