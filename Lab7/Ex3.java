import java.util.concurrent.Semaphore;

public class Ex3 {
    public static void main(String[] args) {
        Semaphore ticket = new Semaphore(4);

        for(int i = 0; i < 10; i++) {
            Thread storeCustomerThread = new Thread(new StoreCustomer(ticket));
            storeCustomerThread.start();
        }

    }
}

class StoreCustomer implements Runnable {
    private Semaphore ticket;

    StoreCustomer(Semaphore ticket) {
        this.ticket = ticket;
    }

    public void run() {
        try {
            ticket.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": Entering the store.");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": Exiting the store.");
        ticket.release();
    }
}