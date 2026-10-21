
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ExecutorExample {
    public static void main(String[] args) {
        // Example usage of ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Runnable task1 = () -> {
            System.out.println("Task 1 executed by " + Thread.currentThread().getName());
            for (int i = 10; i <= 15; i++) {
                System.out.println(Thread.currentThread().getName() + " printing: " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // restore the cleared interrupt flag
                    break;                              // honor cancellation, stop looping
                }
            }
        };

        Runnable task2 = () -> {
            System.out.println("Task 2 executed by " + Thread.currentThread().getName());
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + " printing: " + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // restore the cleared interrupt flag
                    break;                              // honor cancellation, stop looping
                }
            }
        };

        executor.submit(task1);
        executor.submit(task2);
        executor.shutdown();
    }
}
