import java.util.*;

/**
 * Min Cost to Connect All Points (Prim's MST, O(n^2) dense version).
 *
 * Uses an array-based selection of the next closest unvisited vertex rather
 * than a heap, which is optimal for dense complete graphs.
 */
public class MinCostPrimsOptimisedV2 {

    public int minCostConnectPoints(int[][] points) {
        int n = points.length;
        boolean[] visited = new boolean[n];
        double[] minDist = new double[n];
        Arrays.fill(minDist, Double.POSITIVE_INFINITY);
        minDist[0] = 0;

        int total = 0;

        for (int iter = 0; iter < n; iter++) {
            int curr = -1;

            for (int i = 0; i < n; i++) {
                if (!visited[i] && (curr == -1 || minDist[i] < minDist[curr])) {
                    curr = i;
                }
            }

            visited[curr] = true;
            total += (int) minDist[curr];

            int x1 = points[curr][0], y1 = points[curr][1];

            for (int j = 0; j < n; j++) {
                if (!visited[j]) {
                    int x2 = points[j][0], y2 = points[j][1];
                    int cost = Math.abs(x1 - x2) + Math.abs(y1 - y2);
                    minDist[j] = Math.min(minDist[j], cost);
                }
            }
        }

        return total;
    }

    public static void main(String[] args) {
        MinCostPrimsOptimisedV2 solver = new MinCostPrimsOptimisedV2();
        int[][] points = {{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}};
        System.out.println(solver.minCostConnectPoints(points));
    }
}
