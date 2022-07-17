import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Staff implements Runnable {
    private BlockingQueue giftBlockingQueue;
    private Random rand = new Random();

    Staff(BlockingQueue giftBlockingQueue) {
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

class Customer implements Runnable {
    private BlockingQueue giftBlockingQueue;

    Customer(BlockingQueue giftBlockingQueue) {
        this.giftBlockingQueue = giftBlockingQueue;
    }

    public void run() {
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

public class Ex1 {
    public static void main(String[] args) {
        BlockingQueue<Object> giftBlockingQueue = new LinkedBlockingQueue<Object>();

        for(int i = 0; i < 5; i++) {
            Thread staffThread = new Thread(new Staff(giftBlockingQueue));
            staffThread.start();
        }

        for(int i = 0; i < 5; i++) {
            Thread customerThread = new Thread(new Customer(giftBlockingQueue));
            customerThread.start();
        }
    }
}
