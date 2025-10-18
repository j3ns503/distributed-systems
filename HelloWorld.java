public class HelloWorld {
    public static void main(String[] args) {

        // Task 1 -> The Output will still be printed after another
        /*Thread printThread1 = new Thread(new PrintThread1());
        Thread printThread2 = new Thread(new PrintThread2());
        printThread1.start();
        printThread2.start();*/


        //Task 2
        Counter counter = new Counter();
        Thread[] threads = new Thread[20];

        for (int i = 0; i < 20; i++) {
            threads[i] = new Thread(new CounterThread(counter));
            threads[i].start();
        }

        for (int i = 0; i < 20; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Counter Expected: 200000, " + "Counter actual: " + counter.value());
    }
}