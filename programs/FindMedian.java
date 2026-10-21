import java.util.*;

/**
 * Find median from a data stream using two heaps.
 *
 *   - lower: a max-heap holding the smaller half (Python emulates this with a
 *            min-heap of negated values)
 *   - higher: a min-heap holding the larger half
 *
 * addNum(num): insert a number maintaining the heap balance/order invariants.
 * median(): return the running median.
 */
public class FindMedian {
    // lower: max-heap (largest of the smaller half on top)
    private final PriorityQueue<Integer> lower;
    // higher: min-heap (smallest of the larger half on top)
    private final PriorityQueue<Integer> higher;

    public FindMedian() {
        this.lower = new PriorityQueue<>(Collections.reverseOrder());
        this.higher = new PriorityQueue<>();
    }

    public void addNum(int num) {
        higher.add(num);
        lower.add(higher.poll());
        if (lower.size() > higher.size()) {
            higher.add(lower.poll());
        }
    }

    public double median() {
        if ((lower.size() + higher.size()) % 2 == 0) {
            return (higher.peek() + lower.peek()) / 2.0;
        }
        return higher.peek();
    }
}
