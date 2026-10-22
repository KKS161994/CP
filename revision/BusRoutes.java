
import java.util.*;

public class BusRoutes {

    /**
     * InnerBusRoutes
     */
    public record Nodes(int bus, int count) {
    };

    public int numBusesToDestination(
            int[][] routes, int source, int target) {
        if (source == target)
            return 0;
        int counter = 1;
        // Implement your solution here
        Map<Integer, Set<Integer>> busToStops = new HashMap<>();
        Map<Integer, Set<Integer>> stopsToBuses = new HashMap<>();
        Map<Integer, Set<Integer>> busToBuses = new HashMap<>();
        int busCount = 1;

        // build bus to stop and stop to buses
        for (int[] stops : routes) {
            busToStops.putIfAbsent(counter, new HashSet<>());
            for (int stop : stops) {
                stopsToBuses.putIfAbsent(stop, new HashSet<>());
                stopsToBuses.get(stop).add(counter);
                busToStops.get(counter).add(stop);
            }
            counter++;
        }

        int busesCount = busToStops.size();

        // Build bus to buses
        for (counter = 1; counter <= busesCount; counter++) {
            busToBuses.putIfAbsent(counter, new HashSet<>());
            for (int stop : busToStops.get(counter)) {
                busToBuses.get(counter).addAll(stopsToBuses.get(stop));
            }
        }

        Set<Integer> sourceBuses = stopsToBuses.getOrDefault(source, null);
        if (sourceBuses == null)
            return -1;
        Set<Integer> targetBuses = stopsToBuses.getOrDefault(target, null);
        if (targetBuses == null)
            return -1;

        Set<Integer> visited = new HashSet<>();
        ArrayDeque<Nodes> queue = new ArrayDeque<>();
        if (!Collections.disjoint(targetBuses, sourceBuses)) {
            return 1;
        }
        for (int bus : sourceBuses) {
            queue.offer(new Nodes(bus, busCount));
            visited.add(bus);
        }

        while (!queue.isEmpty()) {
            Nodes current = queue.poll();
            int bus = current.bus();
            int count = current.count();

            for (int neighourBus : busToBuses.get(bus)) {
                if (targetBuses.contains(neighourBus))
                    return count + 1;
                if (!visited.contains(neighourBus)) {
                    visited.add(neighourBus);
                    queue.offer(new Nodes(neighourBus, count + 1));
                }
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        BusRoutes sol = new BusRoutes();

        System.out.println(sol.numBusesToDestination(
                new int[][] { { 1, 2, 7 }, { 3, 6, 7 } }, 1, 6)); // 2

        System.out.println(sol.numBusesToDestination(
                new int[][] { { 7, 12 }, { 4, 5, 15 },
                        { 6 }, { 15, 19 }, { 9, 12, 13 } },
                15, 12)); // -1

        System.out.println(sol.numBusesToDestination(
                new int[][] { { 1, 2, 3 } }, 2, 2)); // 0

        System.out.println(sol.numBusesToDestination(
                new int[][] { { 1, 2, 3 } }, 1, 3)); // 1

        System.out.println(sol.numBusesToDestination(
                new int[][] { { 1, 2, 3 }, { 3, 4, 5 },
                        { 5, 6, 7 } },
                1, 7)); // 3
    }
}
