public class MyThread  {
    private static int count = 0;

    public synchronized static void incrementCount() {
        count++;
    }

    public static int getCount() {
        return count;
    }

    
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            for (int i = 0; i < 1000; i++) {
                incrementCount();
            }
        };
        Thread thread1 = new Thread(runnable, "Thread 1");
        Thread thread2 = new Thread(runnable, "Thread 2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        

        System.out.println("Final count: " + getCount());
    }
}