package Lab2;

public class YieldEx {
    public static void main(String[] args) {
        Thread t0 = new Thread(new GoodThread());
        Thread t1 = new Thread(new NormalThread());
        t0.setName("GoodThread");
        t1.setName("NormalThread");
        t0.start();
        t1.start();
    }
}
class GoodThread implements Runnable {
    public void run() {
        for(int x = 1; x <= 10; x++) {
            System.out.println("By " + Thread.currentThread().getName() + ", x is " + x);
            Thread.yield();
        }
    }
}
class NormalThread implements Runnable {
    public void run() {
        for(int x = 1; x <= 10; x++) {
            System.out.println("By " + Thread.currentThread().getName() + ", x is " + x);
        }
    }
}