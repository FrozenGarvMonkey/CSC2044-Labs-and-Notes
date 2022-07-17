import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Q3 {
    public static void main(String[] args) {
        BoundedBuffer boundedBuffer = new BoundedBuffer(3);
        Thread a1 = new Thread(new Aircraft(boundedBuffer, "Taking Off"));
        Thread a2 = new Thread(new Aircraft(boundedBuffer, "Taking Off"));
        Thread a3 = new Thread(new Aircraft(boundedBuffer, "Landing"));
        Thread runway = new Thread(new Runway(boundedBuffer));

        a1.setName("Aircraft 1");
        a2.setName("Aircraft 2");
        a3.setName("Aircraft 3");
        runway.setName("Runway");

        a1.start();
        a2.start();
        a3.start();
        runway.start();
    }
}

class Aircraft implements Runnable {

    private BoundedBuffer boundedBuffer;
    private String flight;

    Aircraft(BoundedBuffer boundedBuffer, String flight) {
        this.boundedBuffer = boundedBuffer;
        this.flight = flight;
    }

    @Override
    public void run() {
        //This is going off the assumption that flights that are landing will be prioritised the least
        if (flight == "Landing"){
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        }

        PrintFlight newPrintingTask = new PrintFlight(Thread.currentThread().getName() + " is " + flight);
        try {
            boundedBuffer.put(newPrintingTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Runway implements Runnable {

    private BoundedBuffer boundedBuffer;

    Runway(BoundedBuffer boundedBuffer) {
        this.boundedBuffer = boundedBuffer;
    }

    @Override
    public void run() {
        while(true) {
            try {
                PrintFlight printingTask = boundedBuffer.get();
                System.out.println(printingTask.getFlightName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class BoundedBuffer {
    private PrintFlight buffer[];
    private int first;
    private int last;
    private int numberOfTaskInBuffer;
    private int size;
    private PrintFlight taskRetrieved;
    private ReentrantLock lock = new ReentrantLock();
    private Condition bufferIsFull = lock.newCondition();
    private Condition bufferIsEmpty = lock.newCondition();

    BoundedBuffer(int length) {
        size = length;
        buffer = new PrintFlight[size];
        last = 0;
        first = 0;
    }

    public void put(PrintFlight printingTask) throws InterruptedException {
        lock.lock();
        while (numberOfTaskInBuffer == 1) {
            // Runway can only handle one aircraft at a time.
            System.out.println("WARNING: RUNWAY IS FULL...");
            bufferIsFull.await();
        }
        numberOfTaskInBuffer++;
        buffer[last] = printingTask;
        last = (last + 1) % size;
        bufferIsEmpty.signalAll();
        lock.unlock();
    }

    public PrintFlight get() throws InterruptedException {
        lock.lock();
        while (numberOfTaskInBuffer == 0) {
            // Wait if buffer is empty.
            bufferIsEmpty.await();
        }
        numberOfTaskInBuffer--;
        taskRetrieved = buffer[first];
        first = (first + 1) % size;
        bufferIsFull.signalAll();
        lock.unlock();
        return taskRetrieved;
    }
}

class PrintFlight {
    private String flightName;
    PrintFlight(String flightName) {
        this.flightName = flightName;
    }

    public String getFlightName() {
        return flightName;
    }
}