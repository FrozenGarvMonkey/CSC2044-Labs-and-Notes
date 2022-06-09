class PrintName implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }
}

public class ThreeThreads {
    public static void main(String[] args) {
        Thread threadA = new Thread(new PrintName());
        Thread threadB = new Thread(new PrintName());
        Thread threadC = new Thread(new PrintName());

        threadA.setName("Thread A");
        threadB.setName("Thread B");
        threadC.setName("Thread C");

        threadA.start();
        threadC.start();

        try {
            threadA.join();
            threadC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadB.start();

        try {
            threadB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //OR
        //while(threadB.isAlive()) {
        //    // Do nothing here.
        //}

        System.out.println("Done");

    }
}
