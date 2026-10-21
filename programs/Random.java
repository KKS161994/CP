import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InsertGetRandom — translated from Python's random.py.
 *
 * Supports insert/remove/getRandom all in O(1) average time:
 *   - a list (`set`) holds the values for O(1) random access
 *   - a map (`map`) maps value -> index in the list
 *   - remove swaps the target with the last element so removal stays O(1)
 *   - random returns a uniformly chosen element
 *
 * NOTE: the file's own class is named Random, which collides with
 * java.util.Random, so java.util.Random is NOT imported and is referenced
 * fully-qualified for the RNG.
 */
public class Random {

    static final class InsertGetRandom {
        private final List<Integer> set;
        private final Map<Integer, Integer> map;
        private final java.util.Random rng;

        InsertGetRandom() {
            this.set = new ArrayList<>();
            this.map = new HashMap<>();
            this.rng = new java.util.Random();
        }

        boolean insert(int val) {
            if (map.containsKey(val)) {
                return false;
            }
            map.put(val, set.size());
            set.add(val);
            return true;
        }

        boolean remove(int val) {
            if (!map.containsKey(val)) {
                return false;
            }
            int index = map.get(val);
            int end = set.get(set.size() - 1);

            set.set(index, end);
            map.put(end, index);

            set.remove(set.size() - 1);
            map.remove(val);

            return true;
        }

        int random() {
            return set.get(rng.nextInt(set.size()));
        }
    }

    public static void main(String[] args) {
        InsertGetRandom r = new InsertGetRandom();
        System.out.println(r.insert(1));  // true
        System.out.println(r.insert(2));  // true
        System.out.println(r.insert(1));  // false (duplicate)
        System.out.println(r.remove(2));  // true
        System.out.println(r.remove(5));  // false (absent)
        r.insert(3);
        System.out.println(r.random());   // 1 or 3
    }
}
