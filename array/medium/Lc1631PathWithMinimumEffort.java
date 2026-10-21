import java.util.*;

/**
 * LC 1631. Path With Minimum Effort
 *
 * URL: https://leetcode.com/problems/path-with-minimum-effort/
 * Difficulty: Medium
 * Tags: Array, Binary Search, Depth-First Search, Breadth-First Search,
 *       Union-Find, Heap (Priority Queue), Matrix
 *
 * You are a hiker preparing for an upcoming hike. You are given heights, a 2D
 * array of size rows x columns, where heights[row][col] represents the height of
 * cell (row, col). You are situated in the top-left cell, (0, 0), and you hope to
 * travel to the bottom-right cell, (rows-1, columns-1) (i.e., 0-indexed). You can
 * move up, down, left, or right, and you wish to find a route that requires the
 * minimum effort.
 *
 * A route's effort is the maximum absolute difference in heights between two
 * consecutive cells of the route.
 *
 * Return the minimum effort required to travel from the top-left cell to the
 * bottom-right cell.
 *
 * Example 1:
 *
 * Input: heights = [[1,2,2],[3,8,2],[5,3,5]]
 * Output: 2
 * Explanation: The route of [1,3,5,3,5] has a maximum absolute difference of 2 in
 * consecutive cells.
 * This is better than the route of [1,2,2,2,5], where the maximum absolute
 * difference is 3.
 *
 * Example 2:
 *
 * Input: heights = [[1,2,3],[3,8,4],[5,3,5]]
 * Output: 1
 * Explanation: The route of [1,2,3,4,5] has a maximum absolute difference of 1 in
 * consecutive cells, which is better than route [1,3,5,3,5].
 *
 * Example 3:
 *
 * Input: heights = [[1,2,1,1,1],[1,2,1,2,1],[1,2,1,2,1],[1,2,1,2,1],[1,1,1,2,1]]
 * Output: 0
 * Explanation: This route does not require any effort.
 *
 * Constraints:
 *
 *     - rows == heights.length
 *
 *     - columns == heights[i].length
 *
 *     - 1 <= rows, columns <= 100
 *
 *     - 1 <= heights[i][j] <= 10^6
 *
 */
public class Lc1631PathWithMinimumEffort {

    private static final int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    public int minimumEffortPath(int[][] heights) {
        int m = heights.length;
        int n = heights[0].length;

        final int INF = Integer.MAX_VALUE;
        int[][] distances = new int[m][n];
        for (int[] row : distances) {
            Arrays.fill(row, INF);
        }
        distances[0][0] = 0;

        // Each entry is (cost, x, y); ordered by cost.
        PriorityQueue<int[]> minQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        minQueue.offer(new int[]{0, 0, 0});

        while (!minQueue.isEmpty()) {
            int[] top = minQueue.poll();
            int cost = top[0], x = top[1], y = top[2];
            if (cost > distances[x][y]) {
                continue;
            }
            for (int[] d : directions) {
                int newX = x + d[0];
                int newY = y + d[1];
                if (isValid(newX, m, newY, n)) {
                    int newCost = Math.max(cost, Math.abs(heights[x][y] - heights[newX][newY]));
                    int originalCost = distances[newX][newY];
                    if (newCost < originalCost) {
                        distances[newX][newY] = newCost;
                        minQueue.offer(new int[]{newCost, newX, newY});
                    }
                }
            }
        }
        return distances[m - 1][n - 1];
    }

    private boolean isValid(int newI, int m, int newJ, int n) {
        return newI >= 0 && newJ >= 0 && newI < m && newJ < n;
    }

    public static void main(String[] args) {
        Lc1631PathWithMinimumEffort s = new Lc1631PathWithMinimumEffort();
        System.out.println(s.minimumEffortPath(new int[][]{{1, 2, 2}, {3, 8, 2}, {5, 3, 5}}));
        System.out.println(s.minimumEffortPath(new int[][]{{1, 2, 3}, {3, 8, 4}, {5, 3, 5}}));
        System.out.println(s.minimumEffortPath(new int[][]{
                {1, 2, 1, 1, 1}, {1, 2, 1, 2, 1}, {1, 2, 1, 2, 1},
                {1, 2, 1, 2, 1}, {1, 1, 1, 2, 1}}));
    }
}
