class PrintName implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}

public class Q1 {
    public static void main(String[] args) {
        Thread t = Thread.currentThread();
        t.setPriority(Thread.MAX_PRIORITY);

        Thread threadA = new Thread(new PrintName());
        Thread threadB = new Thread(new PrintName());
        Thread threadC = new Thread(new PrintName());

        threadA.setName("Thread A");
        threadB.setName("Thread B");
        threadC.setName("Thread C");

        threadA.start();

        try {
            threadA.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        threadB.start();
        
        try {
            threadB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadC.start();

        try {
            threadC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Done");

    }
}
