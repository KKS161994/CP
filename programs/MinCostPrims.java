import java.util.*;

/**
 * 1584. Min Cost to Connect All Points
 * Medium
 *
 * You are given an array points representing integer coordinates of some points
 * on a 2D-plane, where points[i] = [xi, yi].
 *
 * The cost of connecting two points [xi, yi] and [xj, yj] is the manhattan
 * distance between them: |xi - xj| + |yi - yj|, where |val| denotes the
 * absolute value of val.
 *
 * Return the minimum cost to make all points connected. All points are
 * connected if there is exactly one simple path between any two points.
 *
 * (Prim's MST using an explicit graph and a min-heap.)
 */
public class MinCostPrims {

    public int minCostConnectPoints(int[][] points) {
        Map<Integer, List<int[]>> graph = new HashMap<>(); // node -> list of (cost, neighbour)
        int n = points.length;
        for (int i = 0; i < n; i++) {
            int x1 = points[i][0], y1 = points[i][1];
            for (int j = i + 1; j < n; j++) {
                int x2 = points[j][0], y2 = points[j][1];
                int cost = Math.abs(x1 - x2) + Math.abs(y1 - y2);
                graph.computeIfAbsent(i, k -> new ArrayList<>()).add(new int[]{cost, j});
                graph.computeIfAbsent(j, k -> new ArrayList<>()).add(new int[]{cost, i});
            }
        }

        int start = 0;
        // ordered by cost (matches Python heap of (cost, node) tuples)
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            return Integer.compare(a[1], b[1]);
        });
        minHeap.offer(new int[]{0, start});
        Set<Integer> visited = new java.util.HashSet<>(); // fully-qualified: a HashSet.java exists in this package
        int totalCost = 0;

        while (!minHeap.isEmpty()) {
            int[] top = minHeap.poll();
            int cost = top[0], node = top[1];
            if (visited.contains(node)) {
                continue;
            }

            visited.add(node);
            totalCost += cost;

            for (int[] edge : graph.getOrDefault(node, Collections.emptyList())) {
                int neighbour = edge[1];
                if (!visited.contains(neighbour)) {
                    minHeap.offer(new int[]{edge[0], neighbour});
                }
            }
        }
        return totalCost;
    }

    public static void main(String[] args) {
        MinCostPrims solver = new MinCostPrims();
        int[][] points = {{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}};
        System.out.println(solver.minCostConnectPoints(points));
    }
}
