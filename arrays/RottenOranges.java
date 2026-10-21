
import java.util.LinkedList;
import java.util.Queue;

class RottenOranges {

    int[][] directions = {
        {-1, 0}, // Up
        {0, 1}, // Right
        {1, 0}, // Down
        {0, -1} // Left
    };

    public int minTime(int[][] oranges) {
        if (oranges.length == 0) {
            return 0;
        }
        int i, j, minTime = 0, count, freshOranges=0;

        //BFS through queue 
        Queue<int[]> rottenOranges = new LinkedList<>();
        for (i = 0; i < oranges.length; i++) {
            for (j = 0; j < oranges[0].length; j++) {
                if (oranges[i][j] == 2) {
                    rottenOranges.add(new int[]{i, j});
                }
                else if (oranges[i][j] == 1) {
                    freshOranges++;
                }
            }
        }

        while (!rottenOranges.isEmpty()) {
            count = rottenOranges.size();
            while (count > 0) {
                int[] index = rottenOranges.poll();
                i = index[0];
                j = index[1];
                
                for (int[] direction : directions) {
                    int newI = i + direction[0];
                    int newJ = j + direction[1];
                    if (isValid(newI, newJ, oranges)) {
                        rottenOranges.add(new int[]{i + direction[0], j + direction[1]});
                          oranges[newI][newJ]=2;
                          freshOranges--;
                    }
                }              
                count--;
            }
            if(!rottenOranges.isEmpty()) minTime++;
        }

        return freshOranges==0 ? minTime: -1;
    }

    // Validates if indexes are in boundary
    public boolean isValid(int i, int j, int[][] oranges) {
        return i >= 0 && j >= 0 && i < oranges.length && j < oranges[0].length && oranges[i][j] == 1;
    }
}
