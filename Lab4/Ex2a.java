import java.util.Random;

public class Ex2a {

    public static void main(String[] args) {
        SharedStructureReadersPreference ssrp = new SharedStructureReadersPreference();
        Thread w0 = new Thread(new Writer(ssrp));
        Thread w1 = new Thread(new Writer(ssrp));
        Thread w2 = new Thread(new Writer(ssrp));
        Thread r0 = new Thread(new Reader(ssrp));
        Thread r1 = new Thread(new Reader(ssrp));
        Thread r2 = new Thread(new Reader(ssrp));

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

class Reader implements Runnable {
    private SharedStructureReadersPreference ssrp;
    Reader (SharedStructureReadersPreference ssrp) {
        this.ssrp = ssrp;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            try {
                ssrp.startRead();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " : " + ssrp.getValue());
            ssrp.stopRead();
        }
    }
}

class Writer implements Runnable {
    private SharedStructureReadersPreference ssrp;
    Random rand = new Random();
    Writer (SharedStructureReadersPreference ssrp) {
        this.ssrp = ssrp;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            try {
                ssrp.startWrite();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ssrp.increaseValueByOne();
            System.out.println(Thread.currentThread().getName() + " increased value by 1.");
            ssrp.stopWrite();
        }
    }
}

class SharedStructureReadersPreference {
    private int readers = 0;
    private boolean writing = false;
    private int value = 0;
    public synchronized void startWrite() throws InterruptedException {
        while(readers > 0 || writing) {	// Wait until no waiting readers.
            wait();
        }
        writing = true;
    }

    public synchronized void stopWrite() {
        writing = false;
        notifyAll();
    }

    public synchronized void startRead() throws InterruptedException {
        while(writing) { 	            // Wait if writing in progress.
            wait();
        }
        readers++;
    }

    public synchronized void stopRead() {
        readers--;
        notifyAll();
    }

    public void increaseValueByOne() {
        value++;
    }

    public int getValue() {
        return value;
    }

}
