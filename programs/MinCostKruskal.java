import java.util.*;

/**
 * Min Cost to Connect All Points (Kruskal's MST with Disjoint Set Union).
 *
 * You are given an array points representing integer coordinates of some points
 * on a 2D-plane, where points[i] = [xi, yi].
 *
 * The cost of connecting two points is the manhattan distance between them.
 * Return the minimum cost to make all points connected.
 */
public class MinCostKruskal {

    public int minCostConnectPoints(int[][] points) {
        List<int[]> edges = new ArrayList<>(); // (cost, i, j)
        int n = points.length;

        for (int i = 0; i < n; i++) {
            int x1 = points[i][0], y1 = points[i][1];
            for (int j = i + 1; j < n; j++) {
                int x2 = points[j][0], y2 = points[j][1];
                edges.add(new int[]{Math.abs(x1 - x2) + Math.abs(y1 - y2), i, j});
            }
        }

        edges.sort((a, b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            if (a[1] != b[1]) return Integer.compare(a[1], b[1]);
            return Integer.compare(a[2], b[2]);
        });

        DSU dsu = new DSU(n);
        int totalCost = 0;
        int edgesUsed = 0;

        for (int[] edge : edges) {
            int cost = edge[0], u = edge[1], v = edge[2];
            if (dsu.union(u, v)) {
                totalCost += cost;
                edgesUsed++;
                if (edgesUsed == n - 1) {
                    break;
                }
            }
        }
        return totalCost;
    }

    public static void main(String[] args) {
        MinCostKruskal solver = new MinCostKruskal();
        int[][] points = {{0, 0}, {2, 2}, {3, 10}, {5, 2}, {7, 0}};
        System.out.println(solver.minCostConnectPoints(points));
    }
}

class DSU {
    private final int[] parent;
    private final int[] size;

    DSU(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    int find(int x) {
        while (x != parent[x]) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    boolean union(int x, int y) {
        int rx = find(x), ry = find(y);

        if (rx == ry) {
            return false;
        }

        if (size[rx] < size[ry]) {
            int tmp = rx;
            rx = ry;
            ry = tmp;
        }

        parent[ry] = rx;
        size[rx] += size[ry];
        return true;
    }
}
