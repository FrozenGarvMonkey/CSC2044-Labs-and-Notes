import javax.swing.*;
import java.util.Random;
import java.util.concurrent.*;

public class BulkPurchaseUsingBoundedBuffer {
    public static void main(String[] args) {
        BoundedBuffer orderQueue = new BoundedBuffer(100);

        Manufacturer manufacturer = new Manufacturer(orderQueue);
        Thread[] manufacturerThread = new Thread[100];
        for (int i = 0; i < 100; i++) {
            manufacturerThread[i] = new Thread(manufacturer);
            manufacturerThread[i].start();
        }

        Distributor distributor = new Distributor(orderQueue);
        Thread[] distributorThread = new Thread[100];
        for (int i = 0; i < 100; i++) {
            distributorThread[i] = new Thread(distributor);
            distributorThread[i].start();
        }
    }
}

class Manufacturer implements Runnable {
    private BoundedBuffer orderQueue;
    private Random rand = new Random();
    Manufacturer(BoundedBuffer orderQueue) {
        this.orderQueue = orderQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            int unit = rand.nextInt(100) + 1;
            try {
                orderQueue.put(unit);
                System.out.println(Thread.currentThread().getName() + " is selling " + unit + " unit(s)");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Distributor implements Runnable {
    private BoundedBuffer orderQueue;
    private int totalUnitBought = 0;
    Distributor(BoundedBuffer orderQueue) {
        this.orderQueue = orderQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                int unit = orderQueue.take();
                totalUnitBought = totalUnitBought + unit;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class BoundedBuffer {
    private int buffer[];
    private int unitRetrieved;
    private int first, last, size;
    private int numberOfOrderInBuffer;

    BoundedBuffer(int length) {
        size = length;
        buffer = new int[size];
        last = 0;
        first = 0;
    }

    public synchronized void put(int unit) throws InterruptedException {
        while (numberOfOrderInBuffer == size) {
            wait();
        }
        numberOfOrderInBuffer++;
        buffer[last] = unit;
        last = (last + 1) % size;
        notifyAll();
    }

    public synchronized int take() throws InterruptedException {
        while (numberOfOrderInBuffer == 0) {
            wait();
        }
        numberOfOrderInBuffer--;
        unitRetrieved = buffer[first];
        first = (first + 1) % size;
        notifyAll();
        return unitRetrieved;
    }
}