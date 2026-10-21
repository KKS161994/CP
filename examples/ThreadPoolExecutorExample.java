
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ThreadPoolExecutorExample {

    public static void main(String[] args) {
        Callable callable = () -> {
            System.out.println("Callable task is running");
            return "Callable result";
        };

        // Example usage of ThreadPoolExecutor
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        executor.execute(() -> {
            System.out.println("Task 1 is running");
        });
        executor.execute(() -> {
            System.out.println("Task 2 is running");
        });
        // Running callable using executor 
        executor.submit(callable);
        executor.shutdown();
    }
}