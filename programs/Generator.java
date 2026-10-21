import java.util.NoSuchElementException;

/**
 * Python generators, translated to Java.
 *
 * The original Python module had two generators:
 *
 *   1. my_gen() — a two-way generator using `x = yield "First Item"`.
 *      Python's `send()` resumes the generator and passes a value back in at
 *      the suspended `yield`. Java has no `yield` coroutine, so we model the
 *      explicit suspended state with a small state machine that exposes
 *      next() (advance, get next yielded value) and send(value) (advance,
 *      feeding `value` into the prior yield expression).
 *
 *   2. async def gen() — an async generator yielding 0,1,2 with a 1s sleep
 *      before each. Modeled here as an Iterator<Integer> that sleeps 1s in
 *      next() (the Java analog of `await asyncio.sleep(1)` before each yield).
 */
public class Generator {

    // ---- Analog of Python's two-way generator my_gen() ----
    // NOTE: Python `send()`/`next()` on a generator is modeled as an explicit
    // state machine. There is no native coroutine `yield` in Java.
    static final class MyGen {
        private int state = 0; // 0=not started, 1=after first yield, 2=done

        // next() with no value (like Python next(g)).
        String next() {
            return send(null);
        }

        // send(value) — resume, injecting `value` as the result of the
        // suspended `yield`. The very first call must be next()/send(null) to
        // prime, matching Python semantics.
        String send(String value) {
            switch (state) {
                case 0:
                    state = 1;
                    return "First Item"; // x = yield "First Item"
                case 1:
                    System.out.println("Generator received: " + value);
                    state = 2;
                    return "Second Item"; // yield "Second Item"
                default:
                    throw new NoSuchElementException("StopIteration");
            }
        }
    }

    // ---- Analog of Python's async generator gen() ----
    // NOTE: `await asyncio.sleep(1)` before each `yield i` is modeled by
    // sleeping 1s inside next(); the consumer drives it one item at a time.
    static final class AsyncGen implements java.util.Iterator<Integer> {
        private int i = 0;
        private final int limit;

        AsyncGen(int limit) {
            this.limit = limit;
        }

        @Override
        public boolean hasNext() {
            return i < limit;
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            try {
                Thread.sleep(1000); // await asyncio.sleep(1)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return i++;
        }
    }

    public static void main(String[] args) {
        // Mirrors the active part of the Python module: asyncio.run(main()),
        // which printed the first three items of the async generator.
        AsyncGen g = new AsyncGen(3);
        System.out.println(g.next()); // anext(g)
        System.out.println(g.next());
        System.out.println(g.next());
    }
}
