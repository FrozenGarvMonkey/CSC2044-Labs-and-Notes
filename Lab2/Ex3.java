package Lab2;

class Counter {
    private int value = 0;

    public synchronized void setCounterValue(int newValue) {
        value = newValue;
    }

    public synchronized void increaseCounterValueByOne() {
        value = value + 1;
    }

    public synchronized int getCounterValue() {
        return value;
    }
}

class CountWorker implements Runnable {
    Counter counter;

    CountWorker(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        for (int i = 0;i<10000;i++) {
            counter.increaseCounterValueByOne();
        }
    }
}

public class Ex3 {
    public static void main(String[] args) {
        Counter counter = new Counter();
        Thread t0 = new Thread(new CountWorker(counter));
        Thread t1 = new Thread(new CountWorker(counter));
        Thread t2 = new Thread(new CountWorker(counter));
        Thread t3 = new Thread(new CountWorker(counter));
        Thread t4 = new Thread(new CountWorker(counter));
        Thread t5 = new Thread(new CountWorker(counter));

        long start = System.currentTimeMillis();

        t0.start();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        while(t0.isAlive()||t1.isAlive()||t2.isAlive()||t3.isAlive()||t4.isAlive()||t5.isAlive()) {
            //Do nothing here.
            //Wait for all of them to complete before proceed to print out the final value.
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Final Value: " + counter.getCounterValue());
        System.out.println("Elapsed Time: " + timeElapsed);
    }
}
