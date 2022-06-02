import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Ex2a {
    public static void main(String[] args) {
        PrinterNoQueue pnq = new PrinterNoQueue();
        ComputerNoQueue cnq1 = new ComputerNoQueue((pnq));
        ComputerNoQueue cnq2 = new ComputerNoQueue((pnq));
        Thread pnqThread = new Thread(pnq);
        Thread cnqThread0 = new Thread(cnq1);
        Thread cnqThread1 = new Thread(cnq2);

        pnqThread.setName("Printer");
        cnqThread0.setName("Computer A");
        cnqThread1.setName("Computer B");

        pnqThread.start();
        cnqThread0.start();
        cnqThread1.start();
    }
}

class PrinterNoQueue implements Runnable {
    private boolean hasTask = false;
    private String fileName;
    final public ReentrantLock lock = new ReentrantLock();
    final public Condition waitForPrinterToFinish = lock.newCondition();

    public void run() {
        while(true) {
            checkTask();
        }
    }

    public void checkTask() {
        lock.lock();
        while (hasTask == false) {
            try {
                waitForPrinterToFinish.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Printing " + fileName);
        System.out.println("Printing in progress...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        hasTask = false;
        lock.unlock();
    }

    public void setTask(String fileName) {
        this.fileName = fileName;
        hasTask = true;
    }

    public boolean getHasTask() {
        return hasTask;
    }
}

class ComputerNoQueue implements Runnable {

    private PrinterNoQueue pnq;
    private int taskID = 0;

    ComputerNoQueue(PrinterNoQueue pnq) {
        this.pnq = pnq;
    }

    public void run() {
        while(taskID < 3) {
            pnq.lock.lock();
            if (pnq.getHasTask() == false) {
                pnq.setTask(Thread.currentThread().getName() + " Task " + taskID);
                taskID++;
                pnq.waitForPrinterToFinish.signalAll();
            }
            pnq.lock.unlock();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}