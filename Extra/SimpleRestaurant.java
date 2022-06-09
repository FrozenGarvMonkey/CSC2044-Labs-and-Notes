import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Order {
    // To store the name of the dish that one of the chefs will be cooking.
    private String text;
    // To reflect whether there is any order that needs to be processed.
    private boolean orderStatus;

    public Order(String text, boolean orderStatus) {
        this.text = text;
        this.orderStatus = orderStatus;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus) {
        this.orderStatus = orderStatus;
    }
}

public class SimpleRestaurant {
    public static void main(String[] args) {

        final ReentrantLock lock = new ReentrantLock();
        final Condition cond = lock.newCondition();
        Order order = new Order("No Order", true);

        Waitress waitress = new Waitress(order,cond,lock);
        Thread waitressThread = new Thread(waitress, "Waitress");
        waitressThread.start();

        Chef chef1 = new Chef(order,cond,lock);
        Thread chef1Thread = new Thread(chef1, "Chef1");
        chef1Thread.start();

        Chef chef2 = new Chef(order,cond,lock);
        Thread chef2Thread = new Thread(chef2, "Chef2");
        chef2Thread.start();
    }
}

class Chef implements Runnable {
    Order order;
    Condition cond;
    ReentrantLock lock;

    public Chef(Order order, Condition cond, ReentrantLock lock) {
        this.order = order;
        this.cond = cond;
        this.lock = lock;
    }

    public void run() {
        lock.lock();
        try {
            while(order.getOrderStatus()) {
                cond.await();
            }
        } catch (InterruptedException ex) { }
        System.out.println("Chef: I will process the order.");
        order.setOrderStatus(true);
        cond.signal();
        lock.unlock();
    }
}

class Waitress implements Runnable {
    Order order;
    Condition cond;
    ReentrantLock lock;

    public Waitress (Order order, Condition cond, ReentrantLock lock) {
        this.order = order;
        this.cond = cond;
        this.lock = lock;
    }

    public void run() {
        lock.lock();
        System.out.println("Waitress: Here is the order.");
        order.setText("Pasta");
        order.setOrderStatus(false);
        cond.signal();
        try {
            cond.await();
        } catch (InterruptedException ex) {}
        System.out.println("Waitress: I will deliver the order now.");
        lock.unlock();
    }
}
