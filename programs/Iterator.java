import java.util.Arrays;
import java.util.List;

/**
 * Different ways to iterate, translated from Python's iterator.py.
 *
 * The Python file demonstrated several iteration idioms over a list:
 *   - manual next() with a sentinel
 *   - for over a list
 *   - for over an explicit iterator
 *   - while loop catching StopIteration
 *   - while loop with a None sentinel
 *   - iter(callable, sentinel) form
 *
 * NOTE: the simple name `Iterator` collides with java.util.Iterator, so this
 * file does NOT import it; the JDK iterator is referenced fully-qualified as
 * java.util.Iterator where needed.
 */
public class Iterator {

    static List<Integer> sampleList() {
        return Arrays.asList(1, 2, 3, 4);
    }

    // while ((element := next(iterator, None)) is not None): print(element)
    static void sampleIterator() {
        List<Integer> myList = sampleList();
        java.util.Iterator<Integer> iterator = myList.iterator();
        Integer element;
        while ((element = (iterator.hasNext() ? iterator.next() : null)) != null) {
            System.out.println(element);
        }
    }

    // for element in my_list: print(element)
    static void forOverList() {
        List<Integer> myList = sampleList();
        for (Integer element : myList) {
            System.out.println(element);
        }
    }

    // for element in iter(my_list): print(element)
    static void forOverIterator() {
        List<Integer> myList = sampleList();
        java.util.Iterator<Integer> iterator = myList.iterator();
        while (iterator.hasNext()) {
            Integer element = iterator.next();
            System.out.println(element);
        }
    }

    // while True: try next() except StopIteration: break
    static void whileWithStopIteration() {
        List<Integer> myList = sampleList();
        java.util.Iterator<Integer> iterator = myList.iterator();
        while (true) {
            Integer element;
            try {
                element = iterator.next(); // throws NoSuchElementException (StopIteration)
            } catch (java.util.NoSuchElementException e) {
                break;
            }
            System.out.println(element);
        }
    }

    // while True: element = next(iterator, None); if element is None: break
    static void whileWithSentinel() {
        List<Integer> myList = sampleList();
        java.util.Iterator<Integer> iterator = myList.iterator();
        while (true) {
            Integer element = iterator.hasNext() ? iterator.next() : null;
            if (element == null) {
                break;
            }
            System.out.println(element);
        }
    }

    // for element in iter(lambda: next(iterator, None), None): print(element)
    static void iterWithSentinel() {
        List<Integer> myList = sampleList();
        java.util.Iterator<Integer> iterator = myList.iterator();
        java.util.function.Supplier<Integer> callable =
                () -> iterator.hasNext() ? iterator.next() : null;
        Integer element;
        while ((element = callable.get()) != null) {
            System.out.println(element);
        }
    }

    public static void main(String[] args) {
        Object[][] fns = {
                {"sample_iterator", (Runnable) Iterator::sampleIterator},
                {"for_over_list", (Runnable) Iterator::forOverList},
                {"for_over_iterator", (Runnable) Iterator::forOverIterator},
                {"while_with_stopiteration", (Runnable) Iterator::whileWithStopIteration},
                {"while_with_sentinel", (Runnable) Iterator::whileWithSentinel},
                {"iter_with_sentinel", (Runnable) Iterator::iterWithSentinel},
        };
        for (Object[] entry : fns) {
            System.out.println("--- " + entry[0] + " ---");
            ((Runnable) entry[1]).run();
        }
    }
}
