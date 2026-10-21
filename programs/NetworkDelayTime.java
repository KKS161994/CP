import java.util.*;

/**
 * 743. Network Delay Time (Dijkstra's shortest path).
 *
 * Given a network of n nodes labeled 1..n, and a list of travel times as
 * directed edges times[i] = (source, destination, time), return the time it
 * takes for all n nodes to receive a signal sent from node k, or -1 if it is
 * impossible for all nodes to receive the signal.
 */
public class NetworkDelayTime {

    public int networkDelayTime(int[][] times, int n, int k) {
        Map<Integer, List<int[]>> graph = new HashMap<>(); // source -> list of (destination, time)
        for (int[] edge : times) {
            int source = edge[0], destination = edge[1], time = edge[2];
            graph.computeIfAbsent(source, key -> new ArrayList<>()).add(new int[]{destination, time});
        }

        Map<Integer, Double> distances = new HashMap<>();
        for (int node = 1; node <= n; node++) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        distances.put(k, 0.0);

        // priority queue of (distance, node) ordered by distance
        PriorityQueue<double[]> priorityQueue = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) return Double.compare(a[0], b[0]);
            return Double.compare(a[1], b[1]);
        });
        priorityQueue.offer(new double[]{0.0, k});

        while (!priorityQueue.isEmpty()) {
            double[] top = priorityQueue.poll();
            double distance = top[0];
            int node = (int) top[1];
            if (distance > distances.get(node)) {
                continue;
            }

            for (int[] edge : graph.getOrDefault(node, Collections.emptyList())) {
                int neighbourNode = edge[0], time = edge[1];
                double newDistance = distances.get(node) + time;
                if (newDistance < distances.get(neighbourNode)) {
                    distances.put(neighbourNode, newDistance);
                    priorityQueue.offer(new double[]{newDistance, neighbourNode});
                }
            }
        }

        double answer = Double.NEGATIVE_INFINITY;
        for (double d : distances.values()) {
            answer = Math.max(answer, d);
        }

        return answer != Double.POSITIVE_INFINITY ? (int) answer : -1;
    }

    public static void main(String[] args) {
        NetworkDelayTime solver = new NetworkDelayTime();
        int[][] times = {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}};
        System.out.println(solver.networkDelayTime(times, 4, 2));
    }
}
