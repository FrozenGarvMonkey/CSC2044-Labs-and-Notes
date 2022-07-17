
class Order {
    private int order = 0;
    private int serveOrder = 0;
    private boolean cooking = false;

    public synchronized void addOrder(int number) {
        while(cooking){
            try {
                wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        order += 1;
        System.out.println("Waiter " + number + " has received order " + order);
        notify();
    }

    public synchronized void finishOrder(int number) {
        while (order == 0){
            try {
                wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }

        }
        try {
            cooking = true;
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serveOrder += 1;
        System.out.println("Chef " + number + " has finished order " + serveOrder);
        order -= 1;
        notify();
    }

    public synchronized void serveOrder(int number){
        while (serveOrder == 0){
            try {
                wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println("Waiter " + number + " has served order " + serveOrder);
        serveOrder -= 1;
        notify();
    }
}

class Chef implements Runnable {
    Order order;
    int number;

    Chef(Order order, int number) {
        this.order = order;
        this.number = number;
    }

    public void run() {
            order.finishOrder(number);
    }
}

class Waiter implements Runnable {
    Order order;
    int number;
    Waiter(Order order, int number){
        this.order = order;
        this.number = number;
    }

    public void run(){
        order.addOrder(number);
        order.serveOrder(number);
    }
}

public class Ex4 {
    public static void main(String[] args) {
        Order order = new Order();
        Thread waiter1 = new Thread(new Waiter(order, 1));
        Thread waiter2 = new Thread(new Waiter(order, 2));
        Thread chef1 = new Thread(new Chef(order, 1));
        Thread chef2 = new Thread(new Chef(order, 2));

        waiter1.start();
        waiter2.start();
        chef1.start();
        chef2.start();
    }
}