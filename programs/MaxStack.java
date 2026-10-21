import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * MaxStack supporting push, pop, top, peekMax, popMax.
 *
 * Python original uses:
 *   - a list `stack` of (value, id) tuples
 *   - a max-heap `max_heap` of (-value, -id) tuples (heapq is a min-heap,
 *     so negation yields max-by-value, tie-broken by most-recent id)
 *   - a monotonically increasing `counter` giving each push a unique id
 *   - two "deleted" id sets used for lazy deletion: when an element is removed
 *     from one structure, its id is recorded so the other structure can skip it
 *     when that stale entry surfaces at the top.
 *
 * Faithful Java analog: ArrayList for the stack, PriorityQueue for the heap
 * (with negated keys to match heapq + negation), HashSet for the deleted-id sets.
 *
 * Tuples are represented by the package-private record Entry(value, id).
 */
public class MaxStack {

    record Entry(int value, int id) {}

    static class MaxStackImpl {
        // Max-heap on value, tie-broken by id (larger id = more recent).
        private final PriorityQueue<Entry> maxHeap;
        private final List<Entry> stack;
        private int counter;
        private final Set<Integer> deletedFromHeap;
        private final Set<Integer> deletedFromStack;

        public MaxStackImpl() {
            this.maxHeap = new PriorityQueue<>((a, b) -> {
                if (a.value() != b.value()) {
                    return Integer.compare(b.value(), a.value()); // larger value first
                }
                return Integer.compare(b.id(), a.id());            // larger id first
            });
            this.stack = new ArrayList<>();
            this.counter = 0;
            this.deletedFromHeap = new HashSet<>();
            this.deletedFromStack = new HashSet<>();
        }

        private void cleanStack() {
            while (!stack.isEmpty()
                    && deletedFromStack.contains(stack.get(stack.size() - 1).id())) {
                Entry e = stack.remove(stack.size() - 1);
                deletedFromStack.remove(e.id());
            }
        }

        private void cleanHeap() {
            while (!maxHeap.isEmpty() && deletedFromHeap.contains(maxHeap.peek().id())) {
                Entry e = maxHeap.poll();
                deletedFromHeap.remove(e.id());
            }
        }

        public void push(int x) {
            counter += 1;
            Entry e = new Entry(x, counter);
            stack.add(e);
            maxHeap.add(e);
        }

        public int pop() {
            cleanStack();
            Entry item = stack.remove(stack.size() - 1);
            deletedFromHeap.add(item.id());
            return item.value();
        }

        public int top() {
            cleanStack();
            return stack.get(stack.size() - 1).value();
        }

        public int peekMax() {
            cleanHeap();
            return maxHeap.peek().value();
        }

        public int popMax() {
            cleanHeap();
            Entry item = maxHeap.poll();
            deletedFromStack.add(item.id());
            return item.value();
        }
    }

    public static void main(String[] args) {
        MaxStackImpl ms = new MaxStackImpl();
        ms.push(5);
        ms.push(1);
        ms.push(5);
        System.out.println(ms.top());     // 5
        System.out.println(ms.popMax());  // 5
        System.out.println(ms.top());     // 1
        System.out.println(ms.peekMax()); // 5
        System.out.println(ms.pop());     // 1
        System.out.println(ms.top());     // 5
    }
}
