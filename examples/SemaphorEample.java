
import java.util.concurrent.Semaphore;

class SemaphoreExample {
    public static void main(String[] args) {
        // Create a semaphore with 2 permits
        Semaphore semaphore = new Semaphore(2);

        // Create and start 5 threads that will try to acquire the semaphore
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadId + " is trying to acquire the semaphore.");
                    semaphore.acquire();
                    System.out.println("Thread " + threadId + " has acquired the semaphore.");
                    // Simulate some work with the resource
                    Thread.sleep(2000);
                    System.out.println("Thread " + threadId + " is releasing the semaphore.");
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}