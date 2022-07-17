import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Ex4 {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(4,new GameStart());

        for(int i = 0; i < 4; i++) {
            Thread playerThread = new Thread(new Player(barrier));
            playerThread.start();
        }
    }
}

class GameStart implements Runnable {
    public void run() {
        System.out.println("Game Start");
    }
}

class Player implements Runnable {
    private CyclicBarrier startGameBarrier;
    private Random rand = new Random();

    Player(CyclicBarrier startGameBarrier) {
        this.startGameBarrier = startGameBarrier;
    }

    @Override
    public void run() {
        int delay = rand.nextInt(5000)+1000;
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ": I'm ready to start.");
        try {
            startGameBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

    }
}