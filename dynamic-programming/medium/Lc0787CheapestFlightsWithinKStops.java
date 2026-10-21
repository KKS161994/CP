import java.util.*;

/**
 * LC 787. Cheapest Flights Within K Stops
 *
 * URL: https://leetcode.com/problems/cheapest-flights-within-k-stops/
 * Difficulty: Medium
 * Tags: Dynamic Programming, Depth-First Search, Breadth-First Search,
 *       Graph Theory, Heap (Priority Queue), Shortest Path
 *
 * There are n cities connected by some number of flights. You are given an array
 * flights where flights[i] = [fromi, toi, pricei] indicates that there is a
 * flight from city fromi to city toi with cost pricei.
 *
 * You are also given three integers src, dst, and k, return the cheapest price
 * from src to dst with at most k stops. If there is no such route, return -1.
 *
 * Example 1:
 *
 * Input: n = 4, flights = [[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]],
 * src = 0, dst = 3, k = 1
 * Output: 700
 * Explanation:
 * The graph is shown above.
 * The optimal path with at most 1 stop from city 0 to 3 is marked in red and has
 * cost 100 + 600 = 700.
 * Note that the path through cities [0,1,2,3] is cheaper but is invalid because
 * it uses 2 stops.
 *
 * Example 2:
 *
 * Input: n = 3, flights = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 1
 * Output: 200
 * Explanation:
 * The graph is shown above.
 * The optimal path with at most 1 stop from city 0 to 2 is marked in red and has
 * cost 100 + 100 = 200.
 *
 * Example 3:
 *
 * Input: n = 3, flights = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 0
 * Output: 500
 * Explanation:
 * The graph is shown above.
 * The optimal path with no stops from city 0 to 2 is marked in red and has
 * cost 500.
 *
 * Constraints:
 *
 *     - 2 <= n <= 100
 *
 *     - 0 <= flights.length <= (n * (n - 1) / 2)
 *
 *     - flights[i].length == 3
 *
 *     - 0 <= fromi, toi < n
 *
 *     - fromi != toi
 *
 *     - 1 <= pricei <= 10^4
 *
 *     - There will not be any multiple flights between two cities.
 *
 *     - 0 <= src, dst, k < n
 *
 *     - src != dst
 *
 */
public class Lc0787CheapestFlightsWithinKStops {

    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        final int INF = Integer.MAX_VALUE;
        if (src == dst) {
            return 0;
        }
        Map<Integer, List<int[]>> graph = formGraph(flights);

        // distances[city][flights_used]
        int[][] distances = new int[n][k + 2];
        for (int[] row : distances) {
            Arrays.fill(row, INF);
        }
        distances[src][0] = 0;

        // (cost, node, flights_used); ordered by cost.
        PriorityQueue<int[]> minQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        minQueue.offer(new int[]{0, src, 0});

        while (!minQueue.isEmpty()) {
            int[] top = minQueue.poll();
            int cost = top[0], current = top[1], stops = top[2];

            if (distances[current][stops] < cost || stops >= k + 1) {
                continue;
            }

            for (int[] edge : graph.getOrDefault(current, Collections.emptyList())) {
                int neighbour = edge[0], neiCost = edge[1];
                int newCost = cost + neiCost;
                if (newCost < distances[neighbour][stops + 1]) {
                    distances[neighbour][stops + 1] = newCost;
                    minQueue.offer(new int[]{newCost, neighbour, stops + 1});
                }
            }
        }

        int answer = INF;
        for (int d : distances[dst]) {
            answer = Math.min(answer, d);
        }
        return answer != INF ? answer : -1;
    }

    private Map<Integer, List<int[]>> formGraph(int[][] flights) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] f : flights) {
            int source = f[0], destination = f[1], cost = f[2];
            graph.computeIfAbsent(source, key -> new ArrayList<>()).add(new int[]{destination, cost});
        }
        return graph;
    }

    public static void main(String[] args) {
        Lc0787CheapestFlightsWithinKStops s = new Lc0787CheapestFlightsWithinKStops();
        System.out.println(s.findCheapestPrice(4, new int[][]{
                {0, 1, 100}, {1, 2, 100}, {2, 0, 100}, {1, 3, 600}, {2, 3, 200}}, 0, 3, 1));
        System.out.println(s.findCheapestPrice(3, new int[][]{
                {0, 1, 100}, {1, 2, 100}, {0, 2, 500}}, 0, 2, 1));
        System.out.println(s.findCheapestPrice(3, new int[][]{
                {0, 1, 100}, {1, 2, 100}, {0, 2, 500}}, 0, 2, 0));
    }
}
