import java.util.*;

/**
 * Time-based key-value store ("OpenMap").
 *
 * set(key, value, timestamp): store value with timestamp
 * get(key, timestamp): return value with the largest timestamp <= given timestamp
 *
 * Behavior:
 *   set("foo", "bar", 1)
 *   get("foo", 1) -> "bar"
 *   get("foo", 3) -> "bar"
 *
 *   set("foo", "bar2", 4)
 *   get("foo", 4) -> "bar2"
 *   get("foo", 5) -> "bar2"
 *
 * Assumes set is called with non-decreasing timestamps per key so the list
 * stays sorted, enabling binary search in get.
 */
public class OpenMap {

    // mirrors Python defaultdict(list); each entry stores [timestamp, value]
    private final Map<String, List<Object[]>> defMap;

    public OpenMap() {
        this.defMap = new HashMap<>();
    }

    public void set(String key, String value, int timestamp) {
        defMap.computeIfAbsent(key, k -> new ArrayList<>())
              .add(new Object[]{timestamp, value});
    }

    public String get(String key, int timestamp) {
        List<Object[]> valuesList = defMap.getOrDefault(key, new ArrayList<>());
        if (valuesList.isEmpty()) {
            return "";
        }

        int left = 0;
        int right = valuesList.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int midTs = (int) valuesList.get(mid)[0];
            if (timestamp < midTs) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        if (right == -1) {
            return "";
        }
        return (String) valuesList.get(right)[1];
    }
}
