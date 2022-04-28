package Lab2;

public class Ex1 {
    int MIN_PRIORITY = 1;
    int MAX_PRIORITY = 10;
    int NORM_PRIORITY = 5;
    public static void main(String[] args) {
        PrintNameAndValue pnav = new PrintNameAndValue();
        Thread t0 = new Thread(pnav);
        Thread t1 = new Thread(pnav);
        Thread t2 = new Thread(pnav);

        // Alternative way to set the priority.
        t0.setPriority(Thread.MIN_PRIORITY);
        t1.setPriority(Thread.NORM_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);

        // Check the priority.
        System.out.println("Priority of thread t0 is " + t0.getPriority());
        System.out.println("Priority of thread t1 is " + t1.getPriority());
        System.out.println("Priority of thread t2 is " + t2.getPriority());
        System.out.println("Priority of main thread is " + Thread.currentThread().getPriority());

        t0.start();
        t1.start();
        t2.start();
        System.out.println("This is the end of main() method.");
    }
}

class PrintNameAndValue implements Runnable {
    public void run() {
        for(int x = 1; x <= 10; x++) {
            System.out.println("By " + Thread.currentThread().getName() + ", x is " + x);
        }
    }
}
