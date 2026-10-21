//  Example of using BlockingQueue in Java 
// A BlockingQueue is a thread-safe queue that supports operations that 
// wait for the queue to become non-empty when
//  retrieving an element, and wait 
// ArrayBlockingQueue and LinkedBlockingQueue are two common implementations of BlockingQueue in Java.
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
class BlockingQueueExample {
    public static void main(String[] args) {
        // Create a BlockingQueue with a capacity of 5
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(2);
        // Create a ArrayBlockingQueue with a capacity of 5
        BlockingQueue<String> queue2 = new ArrayBlockingQueue<>(2);
        // Producer thread that adds elements to the queue
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String item = "Item " + i;
                    String item2 = "Item2 " + i;
                    queue.put(item); // This will block if the queue is full
                    queue2.put(item2); // This will block if the queue is full
                    System.out.println("Produced: " + item);
                    System.out.println("Produced: " + item2);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });


        // Consumer thread that takes elements from the queue
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String item = queue.take(); // This will block if the queue is empty
                    String item2 = queue2.take(); // This will block if the queue is empty
                    System.out.println("Consumed: " + item);
                    System.out.println("Consumed: " + item2);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}