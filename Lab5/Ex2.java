
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Ex2 {
    public static void main(String[] args) {
        Order order = new Order();
        Thread bossThread = new Thread(new Boss(order));
        Thread workerThread = new Thread(new Worker(order));
        bossThread.start();
        workerThread.start();
    }
}

class Boss implements Runnable {
    private Order order;
    Boss(Order order) {
        this.order = order;
    }

    @Override
    public void run() {
        while(true) {
            order.addOrder();
        }
    }
}

class Worker implements Runnable {
    private Order order;
    Worker(Order order) {
        this.order = order;
    }

    @Override
    public void run() {
        while(true) {
            order.processOrder();
        }
    }
}

class Order {
    private boolean hasOrder = false;
    private boolean workerReady = false;
    ReentrantLock lock = new ReentrantLock();
    Condition noOrder = lock.newCondition();
    Condition noWorker = lock.newCondition();

    public void addOrder() {
        lock.lock();
        try {
            while (workerReady == false) {
                noWorker.await();
            }
            hasOrder = true;
            System.out.println("Added New Order");
            noOrder.signal();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        //try {
        //    Thread.sleep(1000);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
    }

    public void processOrder() {
        lock.lock();
        try {
            workerReady = true;
            noWorker.signal();
            while (hasOrder == false) {
                noOrder.await();
            }
            workerReady = false;
            System.out.println("Process Order");
            hasOrder = false;
        }catch(InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        //try {
        //    Thread.sleep(1000);
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        //}
    }
}
