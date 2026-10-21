import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Streaming + backpressure, demonstrated three ways.
 *
 * Backpressure is the mechanism by which a SLOW consumer makes a FAST producer
 * slow down. Without it, the producer outpaces the consumer, items pile up in
 * some buffer, and memory grows unboundedly — the classic "queue depth blows
 * up" failure mode.
 *
 * This file shows the same producer/consumer scenario three times:
 *
 *     1. demoNoBackpressure  — unbounded queue. BROKEN: producer dumps all
 *        items at once; peak queue depth equals the entire stream.
 *
 *     2. demoBoundedQueue    — bounded queue. WORKS: producer's put()
 *        blocks once the queue fills, naturally pacing it to the consumer.
 *
 *     3. demoSingleItemHandoff — synchronous handoff (capacity-1 queue), the
 *        analog of Python's async generator: at most ONE item is in flight.
 *
 * NOTE (Python -> Java mapping): Python used asyncio coroutines and
 * asyncio.Queue, where `await queue.put()` blocks on a bounded queue. The Java
 * analog of backpressure is a bounded java.util.concurrent.ArrayBlockingQueue
 * whose put() blocks when full, driven by real Threads. The unbounded
 * asyncio.Queue() maps to a LinkedBlockingQueue (effectively unbounded). The
 * async generator (implicit single-item buffer) maps to an ArrayBlockingQueue
 * of capacity 1.
 */
public class StreamingBackpressure {

    // ---------- Knobs ----------
    static final int N_ITEMS = 20;
    static final long CONSUMER_DELAY_MS = 200; // 0.20s
    static final long PRODUCER_DELAY_MS = 0;   // 0 = as fast as possible

    // ---------- Tiny helpers ----------
    static long START = System.nanoTime();

    static String stamp() {
        double secs = (System.nanoTime() - START) / 1_000_000_000.0;
        return String.format("%5.2fs", secs);
    }

    static void banner(String title) {
        System.out.println("\n" + "=".repeat(72));
        System.out.println("  " + title);
        System.out.println("=".repeat(72));
    }

    static void sleepMs(long ms) {
        if (ms <= 0) {
            return;
        }
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Sentinel object marking end-of-stream (Python used None).
    static final Integer SENTINEL = null;

    // =====================================================================
    // Demo 1 — Unbounded queue. BROKEN.
    // =====================================================================
    static void demoNoBackpressure() throws InterruptedException {
        banner("Demo 1 — UNBOUNDED queue (BROKEN: no backpressure)");
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(); // no maxsize
        AtomicInteger peakDepth = new AtomicInteger(0);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < N_ITEMS; i++) {
                    queue.put(i); // never blocks: unbounded
                    peakDepth.accumulateAndGet(queue.size(), Math::max);
                    System.out.printf("[%s] producer  -> put %2d  (queue depth=%d)%n",
                            stamp(), i, queue.size());
                    sleepMs(PRODUCER_DELAY_MS);
                }
                queue.put(Integer.MIN_VALUE); // sentinel
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Integer item = queue.take();
                    if (item == Integer.MIN_VALUE) {
                        return;
                    }
                    sleepMs(CONSUMER_DELAY_MS);
                    System.out.printf("[%s] consumer <- got %2d  (queue depth=%d)%n",
                            stamp(), item, queue.size());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        long t0 = System.nanoTime();
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        double elapsed = (System.nanoTime() - t0) / 1_000_000_000.0;

        System.out.printf("%n  total time:   %5.2fs%n", elapsed);
        System.out.printf("  peak buffer:  %d items   <-- THE DANGER NUMBER%n", peakDepth.get());
        System.out.println("  ^ Every one of those items lived in memory at the same instant.");
    }

    // =====================================================================
    // Demo 2 — Bounded queue. WORKS.
    // =====================================================================
    static void demoBoundedQueue() throws InterruptedException {
        banner("Demo 2 — BOUNDED queue (WORKS: backpressure via maxsize)");
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3); // maxsize=3
        AtomicInteger peakDepth = new AtomicInteger(0);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < N_ITEMS; i++) {
                    queue.put(i); // <-- backpressure: blocks when full
                    peakDepth.accumulateAndGet(queue.size(), Math::max);
                    System.out.printf("[%s] producer  -> put %2d  (queue depth=%d)%n",
                            stamp(), i, queue.size());
                    sleepMs(PRODUCER_DELAY_MS);
                }
                queue.put(Integer.MIN_VALUE);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Integer item = queue.take();
                    if (item == Integer.MIN_VALUE) {
                        return;
                    }
                    sleepMs(CONSUMER_DELAY_MS);
                    System.out.printf("[%s] consumer <- got %2d  (queue depth=%d)%n",
                            stamp(), item, queue.size());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        long t0 = System.nanoTime();
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        double elapsed = (System.nanoTime() - t0) / 1_000_000_000.0;

        System.out.printf("%n  total time:   %5.2fs%n", elapsed);
        System.out.printf("  peak buffer:  %d items   <-- bounded by maxsize%n", peakDepth.get());
        System.out.println("  ^ Memory stays flat no matter how big the stream is.");
    }

    // =====================================================================
    // Demo 3 — Single-item handoff. WORKS (analog of async generator).
    // =====================================================================
    static void demoSingleItemHandoff() throws InterruptedException {
        banner("Demo 3 — SINGLE-ITEM HANDOFF (WORKS: implicit single-item buffer)");
        // Capacity-1 queue: at most one item is in flight, exactly like an
        // async generator that pauses at each `yield` until the consumer asks.
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);
        AtomicInteger peakDepth = new AtomicInteger(0);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < N_ITEMS; i++) {
                    System.out.printf("[%s] producer  -> yield %2d%n", stamp(), i);
                    peakDepth.accumulateAndGet(1, Math::max);
                    queue.put(i); // pauses until consumer takes
                    sleepMs(PRODUCER_DELAY_MS);
                }
                queue.put(Integer.MIN_VALUE);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Integer item = queue.take();
                    if (item == Integer.MIN_VALUE) {
                        return;
                    }
                    sleepMs(CONSUMER_DELAY_MS);
                    System.out.printf("[%s] consumer <- got   %2d%n", stamp(), item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        long t0 = System.nanoTime();
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        double elapsed = (System.nanoTime() - t0) / 1_000_000_000.0;

        System.out.printf("%n  total time:   %5.2fs%n", elapsed);
        System.out.printf("  peak buffer:  %d item    <-- generators self-pace%n", peakDepth.get());
    }

    public static void main(String[] args) throws InterruptedException {
        START = System.nanoTime();
        demoNoBackpressure();
        START = System.nanoTime();
        demoBoundedQueue();
        START = System.nanoTime();
        demoSingleItemHandoff();
    }
}
