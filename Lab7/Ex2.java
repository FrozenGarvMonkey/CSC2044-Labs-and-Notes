import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

class FairStaff implements Runnable {
    private BlockingQueue giftBlockingQueue;
    private Random rand = new Random();

    FairStaff(BlockingQueue giftBlockingQueue) {
        this.giftBlockingQueue = giftBlockingQueue;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            int delay = rand.nextInt(1000)+1000;
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("Staff: Putting a gift into the queue.");
                giftBlockingQueue.put(new Object());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class FairCustomer implements Runnable {
    private BlockingQueue giftBlockingQueue;
    private CountDownLatch startSignal;

    FairCustomer(BlockingQueue giftBlockingQueue, CountDownLatch startSignal) {
        this.giftBlockingQueue = giftBlockingQueue;
        this.startSignal = startSignal;
    }

    public void run() {
        System.out.println("Customer: I'm ready.");
        try {
            startSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < 10; i++) {
            try {
                giftBlockingQueue.take();
                System.out.println("Customer: Took a gift from the queue.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Ex2 {
    public static void main(String[] args) {
        BlockingQueue<Object> giftBlockingQueue = new LinkedBlockingQueue<Object>();
        CountDownLatch startSignal = new CountDownLatch(1);

        for(int i = 0; i < 5; i++) {
            Thread staffThread = new Thread(new FairStaff(giftBlockingQueue));
            staffThread.start();
        }

        for(int i = 0; i < 5; i++) {
            Thread customerThread = new Thread(new FairCustomer(giftBlockingQueue, startSignal));
            customerThread.start();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startSignal.countDown();

    }
}