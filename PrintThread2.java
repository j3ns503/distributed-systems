public class PrintThread2 implements Runnable {
    @Override
    public void run() {
        System.out.println("Hello from thread 2!");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Hello from thread 2!");
    }
}
