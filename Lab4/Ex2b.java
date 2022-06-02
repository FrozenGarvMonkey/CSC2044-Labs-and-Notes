import java.util.Random;

public class Ex2b {
    public static void main(String[] args) {
        SharedStructureWritersPreference sswp = new SharedStructureWritersPreference();
        Thread w0 = new Thread(new WriterPref(sswp));
        Thread w1 = new Thread(new WriterPref(sswp));
        Thread w2 = new Thread(new WriterPref(sswp));
        Thread r0 = new Thread(new ReaderLowerPref(sswp));
        Thread r1 = new Thread(new ReaderLowerPref(sswp));
        Thread r2 = new Thread(new ReaderLowerPref(sswp));

        w0.setName("Writer-0");
        w1.setName("Writer-1");
        w2.setName("Writer-2");
        r0.setName("Reader-0");
        r1.setName("Reader-1");
        r2.setName("Reader-2");

        w0.start();
        w1.start();
        w2.start();

        r0.start();
        r1.start();
        r2.start();
    }
}

class ReaderLowerPref implements Runnable {
    private SharedStructureWritersPreference sswp;
    ReaderLowerPref (SharedStructureWritersPreference sswp) {
        this.sswp = sswp;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            try {
                sswp.startRead();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " : " + sswp.getValue());
            sswp.stopRead();
        }
    }
}

class WriterPref implements Runnable {
    private SharedStructureWritersPreference sswp;
    Random rand = new Random();
    WriterPref (SharedStructureWritersPreference sswp) {
        this.sswp = sswp;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            try {
                sswp.startWrite();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sswp.increaseValueByOne();
            System.out.println(Thread.currentThread().getName() + " increased value by 1.");
            sswp.stopWrite();
        }
    }
}

class SharedStructureWritersPreference {
    private int readers = 0;
    private int waitingWriters = 0;
    private boolean writing = false;
    private int value = 0;

    public synchronized void startWrite() throws InterruptedException {
        while(writing) {	        // Wait until it is ok to write.
            waitingWriters++;
            wait();
            waitingWriters--;
        }
        writing = true;
    }

    public synchronized void stopWrite() {
        writing = false;
        notifyAll();
    }

    public synchronized void startRead() throws InterruptedException {
        while(writing || waitingWriters > 0) {      // Wait if writing in progress or there is at least one waiting writer.
            wait();
        }
        readers++;
    }

    public synchronized void stopRead() {
        readers--;
        if (readers == 0) { // Let all readers to finish (batch processing) before writers start to write again.
            // More readers could get the chance to run even when there are large number of writers.
            notifyAll();
        }
    }

    public void increaseValueByOne() {
        value++;
    }

    public int getValue() {
        return value;
    }
}
