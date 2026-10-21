import java.util.*;

/**
 * 1631. Path With Minimum Effort (Dijkstra on a grid).
 *
 * You are given a 2D grid `heights`. A route's effort is the maximum absolute
 * difference in heights between two consecutive cells of the route. Return the
 * minimum effort required to travel from the top-left cell to the bottom-right
 * cell.
 */
public class PathWithMinimalEffort {

    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    public int minPath(int[][] heights) {
        // (effort, i, j) -> effort in reaching node (i, j)
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            if (a[1] != b[1]) return Integer.compare(a[1], b[1]);
            return Integer.compare(a[2], b[2]);
        });
        minHeap.offer(new int[]{0, 0, 0});

        int m = heights.length, n = heights[0].length;
        int[][] distances = new int[m][n];
        for (int[] row : distances) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        distances[0][0] = 0;

        while (!minHeap.isEmpty()) {
            int[] top = minHeap.poll();
            int effort = top[0], i = top[1], j = top[2];

            if (distances[i][j] < effort) {
                continue;
            }

            if (i == m - 1 && j == n - 1) {
                return effort;
            }

            for (int[] dir : DIRECTIONS) {
                int newX = dir[0] + i;
                int newY = dir[1] + j;
                if (isValid(newX, newY, m, n)) {
                    int newEffort = Math.max(effort, Math.abs(heights[newX][newY] - heights[i][j]));
                    if (newEffort < distances[newX][newY]) {
                        distances[newX][newY] = newEffort;
                        minHeap.offer(new int[]{newEffort, newX, newY});
                    }
                }
            }
        }

        return distances[m - 1][n - 1];
    }

    private boolean isValid(int newI, int newJ, int m, int n) {
        return 0 <= newI && newI < m && 0 <= newJ && newJ < n;
    }

    public static void main(String[] args) {
        PathWithMinimalEffort solver = new PathWithMinimalEffort();
        int[][] heights = {{1, 2, 2}, {3, 8, 2}, {5, 3, 5}};
        System.out.println(solver.minPath(heights));
    }
}
