import java.util.Random;

class Round {
    private int roundNumber = 0;
    public int ballsTaken = 0;
    public int aliceBasket = 100;
    public int bobBasket = 100;
    public int catherineBasket = 100;
    Random rand = new Random();

    public synchronized void nextRound(int roundNumber) {
        if (roundNumber < 5) {
            roundNumber++;
            System.out.println(roundNumber + "has ended.");
            notifyAll();
        } else
            System.out.println("All rounds have been completed");
            //System.out.println("All rounds have been completed");
    }

    public synchronized void stealBalls() {
        if (roundNumber > 5) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ballsTaken = rand.nextInt(10 - 1) + 1;
    }
    }

class Alice implements Runnable{
    Round round;
    Alice(Round round){
        this.round = round;
    }
    public void run(){
        for(int i=0;i<5;i++){
            round.stealBalls();
            round.catherineBasket = round.catherineBasket - round.ballsTaken;
            round.aliceBasket = round.aliceBasket + round.ballsTaken;
            System.out.println("Alice has" + round.aliceBasket);
        }
    }
}

class Bob implements Runnable{
    Round round;
    Bob(Round round){
        this.round = round;
    }
    public void run(){
        for(int i=0;i<5;i++){
            round.stealBalls();
            round.aliceBasket = round.aliceBasket - round.ballsTaken;
            round.bobBasket = round.bobBasket + round.ballsTaken;
        }
    }
}

class Catherine implements Runnable{
    Round round;
    Catherine(Round round){
        this.round = round;
    }
    public void run(){
        for(int i=0;i<5;i++){
            round.stealBalls();
            round.bobBasket = round.bobBasket - round.ballsTaken;
            round.catherineBasket = round.catherineBasket + round.ballsTaken;
        }
    }
}
public class Q2_new {
    public static void main(String[] args){
        Round round = new Round();
        Thread alice = new Thread(new Alice(round));
        Thread bob = new Thread(new Bob(round));
        Thread catherine = new Thread(new Catherine(round));

        alice.start();
        bob.start();
        catherine.start();

    }
}