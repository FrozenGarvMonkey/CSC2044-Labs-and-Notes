package Lab1.Ex1;

class MyThread extends Thread {
    public void run() {
        System.out.println("This is example 1.");
    }
}

class MyRunnable implements Runnable {
    public void run() {
        System.out.println("This is a runnable.");
    }
}

public class Ex1 {
    public static void main(String[] args) {
        MyRunnable mr = new MyRunnable();
        Thread t_norm = new Thread(mr);
        MyThread t_cust = new MyThread();
        t_norm.start();
        t_cust.start();
    }
}

/* 

Answer for A)

While regular print statements give the same output.

The above example makes use of threads and has both functions process concurrently. Their output is determined by when they are called, therfore it seems to still be sequential.

*/