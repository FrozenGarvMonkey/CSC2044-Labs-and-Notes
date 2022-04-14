package Lab1.Ex3;

class MyThread implements Runnable {
    private int x = 0;

    public void run() {
        // Will just continue to run without stopping.
        while (true) {
            System.out.println("By " + Thread.currentThread().getName() + ", x is " + x);
            x = x + 1;
        }
    }
}

public class Ex3 {
    public static void main(String[] args) {
        Thread t0 = new Thread(new MyThread());
        // Change the daemon status of thread t0.
        t0.setDaemon(false);
        t0.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        System.out.println("This is the end of main() method.");
    }
}

/* 
b) The Non-Daemon thread runs nearly infinitely until it's eventually set to sleep. The Daemon thread ends it's run as soon as it's parent thread is set to sleep.
*/