class LivelockExample {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();
    //  Demonstrates a livelock where both threads are active but unable to make progress due to constantly yielding to each other.
    public static void main(String[] args) {
    Thread thread1 = new Thread(() -> {
            while (true) {
                synchronized (lock1) {
                    System.out.println("Thread 1: Holding lock 1...");
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                    System.out.println("Thread 1: Yielding to Thread 2...");
                }
                // Yield to allow the other thread to proceed
                Thread.yield();
            }
        });

    Thread thread2 = new Thread(() -> {
            while (true) {
                synchronized (lock2) {
                    System.out.println("Thread 2: Holding lock 2...");
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                    System.out.println("Thread 2: Yielding to Thread 1...");
                }
                // Yield to allow the other thread to proceed
                Thread.yield();
            }
        });
        thread1.start();
        thread2.start();

       }
}