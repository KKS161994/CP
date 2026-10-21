import java.util.HashSet;
import java.util.Set;

public class IslandsCount {
    public static void main(String[] args) {

    }

    int[][] directions = { { 0, 1 }, { 1, 0 }, { -1, 0 }, { 0, -1 } };

    public int numIslands(char[][] grid) {
        // Your implementation
        int count = 0;
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1' && !seen.contains(formKey(i, j))) {
                    seen.add(formKey(i, j));
                    bfs(grid, i, j, seen);
                    count++;
                }
            }
        }
        return count;
    }

    public String formKey(int i, int j) {
        return i + "," + j;
    }

    public void bfs(char[][] grid, int i,
            int j, Set<String> seen) {
        for (int[] direction : directions) {
            int newI = i + direction[0];
            int newJ = j + direction[1];
            if ((newI < 0 || newI >= grid.length) || (newJ < 0 || newJ >= grid[0].length))
                continue;
            if (grid[newI][newJ] == '1' &&
                    !seen.contains(formKey(newI, newJ))) {
                seen.add(formKey(newI, newJ));
                bfs(grid, newI, newJ, seen);

            }
        }
    }

}
