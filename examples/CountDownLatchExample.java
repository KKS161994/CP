
import java.util.concurrent.CountDownLatch;

class CountDownLatchExample {
    public static void main(String[] args) throws InterruptedException {
        int count = 3;
        CountDownLatch latch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            new Thread(new Worker(latch)).start();
        }

        latch.await(); // Wait until the count reaches zero
        System.out.println("All workers have finished their tasks.");
    }

}
class Worker implements Runnable {
    private final CountDownLatch latch;

    public Worker(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            System.out.println("Worker " + Thread.currentThread().getName() + " is doing work...");
            Thread.sleep(1000); // Simulate work
            System.out.println("Worker " + Thread.currentThread().getName() + " has finished work.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            latch.countDown(); // Decrement the count of the latch
        }
    }
}