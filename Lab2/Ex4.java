package Lab2;

class CounterNS {
    private int value = 0;
    public void setCounterValue(int newValue) { value = newValue; }
    public void increaseCounterValueByOne() { value = value + 1; }
    public int getCounterValue() { return value; }
}

class CounterWS {
    private int value = 0;
    public synchronized void setCounterValue(int newValue) { value = newValue; }
    public synchronized void increaseCounterValueByOne() { value = value + 1; }
    public synchronized int getCounterValue() { return value; }
}

class CountWorkerNS implements Runnable {
    CounterNS counter;
    CountWorkerNS(CounterNS counter) { this.counter = counter; }

    @Override
    public void run() {
        for (int i = 0;i<10000;i++) {
            counter.increaseCounterValueByOne();
        }
    }
}

class CountWorkerWS implements Runnable {
    CounterWS counter;
    CountWorkerWS(CounterWS counter) { this.counter = counter; }

    @Override
    public void run() {
        for (int i = 0;i<10000;i++) {
            counter.increaseCounterValueByOne();
        }
    }
}

public class Ex4 {
    public static void main(String[] args) {
        long totalTimeElapsedNS = 0;
        for(int i = 0; i<1000;i++) {
            CounterNS counter = new CounterNS();
            Thread t0 = new Thread(new CountWorkerNS(counter));
            Thread t1 = new Thread(new CountWorkerNS(counter));
            Thread t2 = new Thread(new CountWorkerNS(counter));
            Thread t3 = new Thread(new CountWorkerNS(counter));
            Thread t4 = new Thread(new CountWorkerNS(counter));
            Thread t5 = new Thread(new CountWorkerNS(counter));

            long start = System.currentTimeMillis();

            t0.start();
            t1.start();
            t2.start();
            t3.start();
            t4.start();
            t5.start();

            while (t0.isAlive() || t1.isAlive() || t2.isAlive() || t3.isAlive() || t4.isAlive() || t5.isAlive()) {
                //Do nothing here.
                //Wait for all of them to complete before proceed to print out the final value.
            }

            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            totalTimeElapsedNS = totalTimeElapsedNS + timeElapsed;
        }

        long totalTimeElapsedWS = 0;
        for(int i = 0; i<1000;i++) {
            CounterWS counter = new CounterWS();
            Thread t0 = new Thread(new CountWorkerWS(counter));
            Thread t1 = new Thread(new CountWorkerWS(counter));
            Thread t2 = new Thread(new CountWorkerWS(counter));
            Thread t3 = new Thread(new CountWorkerWS(counter));
            Thread t4 = new Thread(new CountWorkerWS(counter));
            Thread t5 = new Thread(new CountWorkerWS(counter));

            long start = System.currentTimeMillis();

            t0.start();
            t1.start();
            t2.start();
            t3.start();
            t4.start();
            t5.start();

            while (t0.isAlive() || t1.isAlive() || t2.isAlive() || t3.isAlive() || t4.isAlive() || t5.isAlive()) {
                //Do nothing here.
                //Wait for all of them to complete before proceed to print out the final value.
            }

            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            totalTimeElapsedWS = totalTimeElapsedWS + timeElapsed;
        }

        System.out.println("Total Time Elapsed without Synchronization (1000 Rounds):" + totalTimeElapsedNS);
        System.out.println("Total Time Elapsed with Synchronization (1000 Rounds):" + totalTimeElapsedWS);
    }
}
