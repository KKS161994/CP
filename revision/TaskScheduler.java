import java.util.Arrays;

public class TaskScheduler {
    
    public int leastInterval(char[] tasks, int n) {
        int[] remaining = new int[26];
        int[] nextAvailable = new int[26];

        for (char task : tasks) {
            remaining[task - 'A']++;
        }

        int time = 0;
        int tasksLeft = tasks.length;

        while (tasksLeft > 0) {
            int bestTask = -1;
            int earliestTime = Integer.MAX_VALUE;

            for (int i = 0; i < 26; i++) {
                if (remaining[i] == 0) continue;

                if (nextAvailable[i] <= time) {
                    if (bestTask == -1 ||
                        remaining[i] > remaining[bestTask]) {
                        bestTask = i;
                    }
                } else {
                    earliestTime = Math.min(
                        earliestTime, nextAvailable[i]
                    );
                }
            }

            if (bestTask == -1) {
                time = earliestTime;
                continue;
            }

            remaining[bestTask]--;
            tasksLeft--;

            nextAvailable[bestTask] = time + n + 1;
            time++;
        }

        return time;
    }
    
    public static void main(String[] args) {
    TaskScheduler sol = new TaskScheduler();

    System.out.println(sol.leastInterval(
        new char[]{'A','A','A','B','B','B'}, 2)); // 8

    System.out.println(sol.leastInterval(
        new char[]{'A','C','A','B','D','B'}, 1)); // 6

    System.out.println(sol.leastInterval(
        new char[]{'A','A','A','B','B','B'}, 3)); // 10

    System.out.println(sol.leastInterval(
        new char[]{'A','A','A'}, 2)); // 7
}
}


//  [a4 b2 c5] [2]
//  a b a b a c a c _ c
//  abcabcaca