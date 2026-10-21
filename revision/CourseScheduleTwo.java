import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CourseScheduleTwo {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> input = new HashMap<>();
        int[] indegree = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            indegree[i] = 0;
            input.put(i, new ArrayList<>());
        }
        for (int[] preq : prerequisites) {
            input.get(preq[1]).add(preq[0]);
            indegree[preq[0]] = indegree[preq[0]] + 1;
        }
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        List<Integer> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            int currentCourse = queue.poll();
            result.add(currentCourse);
            for (int neighour : input.get(currentCourse)) {
                int updated = indegree[neighour] - 1;
                indegree[neighour] = updated;
                if (updated == 0) {
                    queue.offer(neighour);
                }
            }
        }
        if (result.size() != numCourses) {
            return new int[0];
        }
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
}
