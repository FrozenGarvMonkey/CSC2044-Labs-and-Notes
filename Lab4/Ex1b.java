import javax.swing.*;
import java.util.Random;

public class Ex1b {
    public static void main(String[] args) {
        BoundedBuffer boundedBuffer = new BoundedBuffer(10);
        Thread com1 = new Thread(new Computer(boundedBuffer));
        Thread com2 = new Thread(new Computer(boundedBuffer));
        Thread printer = new Thread(new Printer(boundedBuffer));

        com1.setName("Com 1");
        com2.setName("Com 2");
        printer.setName("Printer");

        com1.start();
        com2.start();
        printer.start();
    }
}

class Computer implements Runnable {

    private BoundedBuffer boundedBuffer;

    Computer(BoundedBuffer boundedBuffer) {
        this.boundedBuffer = boundedBuffer;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            PrintingTask newPrintingTask = new PrintingTask("Task" + i + " from " + Thread.currentThread().getName());
            try {
                boundedBuffer.put(newPrintingTask);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Printer implements Runnable {

    private BoundedBuffer boundedBuffer;

    Printer(BoundedBuffer boundedBuffer) {
        this.boundedBuffer = boundedBuffer;
    }

    @Override
    public void run() {
        while(true) {
            try {
                PrintingTask printingTask = boundedBuffer.get();
                System.out.println("Printing " + printingTask.getFileName());
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
}

class BoundedBuffer {
    private PrintingTask buffer[];
    private int first;
    private int last;
    private int numberOfTaskInBuffer;
    private int size;
    private PrintingTask taskRetrieved;

    BoundedBuffer(int length) {
        size = length;
        buffer = new PrintingTask[size];
        last = 0;
        first = 0;
    }

    public synchronized void put(PrintingTask printingTask) throws InterruptedException {
        while (numberOfTaskInBuffer == size) {
            // Wait if buffer is full.
            System.out.println("WARNING: BUFFER IS FULL...");
            wait();
        }
        numberOfTaskInBuffer++;
        buffer[last] = printingTask;
        last = (last + 1) % size;
        System.out.println("Added One Task:" + printingTask.getFileName());
        notifyAll();
    }

    public synchronized PrintingTask get() throws InterruptedException {
        while (numberOfTaskInBuffer == 0) {
            // Wait if buffer is empty.
            wait();
        }
        numberOfTaskInBuffer--;
        taskRetrieved = buffer[first];
        first = (first + 1) % size;
        notifyAll();
        return taskRetrieved;
    }
}

class PrintingTask {
    private String fileName;
    PrintingTask(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}