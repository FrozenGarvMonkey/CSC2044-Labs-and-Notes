public class Ex1a {
    public static void main(String[] args) {
        PrinterNoQueue pnq = new PrinterNoQueue();
        ComputerNoQueue cnq1 = new ComputerNoQueue((pnq));
        ComputerNoQueue cnq2 = new ComputerNoQueue((pnq));
        Thread pnqThread = new Thread(pnq);
        Thread cnqThread0 = new Thread(cnq1);
        Thread cnqThread1 = new Thread(cnq2);

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

    public void run() {
        while(true) {
            checkTask();
        }
    }

    public synchronized void checkTask() {
        while (hasTask == false) {
            try {
                wait();
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
    }

    public synchronized void setTask(String fileName) {
        this.fileName = fileName;
        hasTask = true;
    }

    public synchronized boolean getHasTask() {
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
            synchronized (pnq) {
                if (pnq.getHasTask() == false) {
                    pnq.setTask(Thread.currentThread().getName() + " Task " + taskID);
                    taskID++;
                    pnq.notifyAll();
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}