package Lab2;

public class Ex2 {
    public static void main ( String[] args ) {
        Thread t0 = new Thread(new MyThread());
        Thread t1 = new Thread(new MyThread());
        Thread t2 = new Thread(new MyThread());
        Thread t3 = new Thread(new MyThread());

        // Start thread t3.
        t3.start();
        // Remember to include in a try-catch block.
        try {
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Start thread t2.
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Start thread t0.
        t0.start();
        try {
            t0.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Start thread t1.
        t1.start();

        System.out.println("This is the end of main() method.");
    }
}

class MyThread implements Runnable {
    public void run() {
        for(int x = 1; x <= 10; x++) {
            System.out.println("By " + Thread.currentThread().getName() + ", x is " + x);
        }
    }
}
