import java.util.Random;

class Steal {
    Random rand = new Random();

    public int alice_basket = 100;
    public int bob_basket = 100;
    public int cat_basket = 100;
    
    public int stolen = 0;

    public synchronized void stealBalls() {
            stolen = rand.nextInt(10) + 1;
    }

    public void GreatestBalls(){
        if (alice_basket > bob_basket && alice_basket > cat_basket){
            System.out.println ("Alice has the highest number of balls: " + alice_basket);
        }
        else if (bob_basket > cat_basket){
            System.out.println ("Bob has the highest number of balls: " + bob_basket);
        }
        else{
            System.out.println("Catherine has highest number of balls: " + cat_basket);
        }
    }
}

class Alice implements Runnable{
    Steal steal;
    Alice(Steal steal){
        this.steal = steal;
    }
    public void run(){
        for(int i=0;i<5;i++){
            steal.stealBalls();
            steal.cat_basket = steal.cat_basket - steal.stolen;
            steal.alice_basket = steal.alice_basket + steal.stolen;
            System.out.println("Alice currently has " + steal.alice_basket + " balls." + "\n Taking " + steal.stolen + " from Catherine.");
        }
    }
}

class Bob implements Runnable{
    Steal steal;
    Bob(Steal steal){
        this.steal = steal;
    }
    public void run(){
        for(int i=0;i<5;i++){
            steal.stealBalls();
            steal.alice_basket = steal.alice_basket - steal.stolen;
            steal.bob_basket = steal.bob_basket + steal.stolen;
            System.out.println("Bob currently has " + steal.bob_basket + " balls." + "\n Taking " + steal.stolen + " from Alice.");
        }
    }
}

class Catherine implements Runnable{
    Steal steal;
    Catherine(Steal steal){
        this.steal = steal;
    }
    public void run(){
        for(int i=0;i<5;i++){
            steal.stealBalls();
            steal.bob_basket = steal.bob_basket - steal.stolen;
            steal.cat_basket = steal.cat_basket + steal.stolen;
            System.out.println("Catherine has " + steal.cat_basket + " balls." + "\n Taking " + steal.stolen + " from Bob.");       }
    }
}

public class Q2 {
    public static void main(String[] args){
        
        Steal round = new Steal();

        Thread Alice = new Thread(new Alice(round));
        Thread Bob = new Thread(new Bob(round));
        Thread Catherine = new Thread(new Catherine(round));

        Alice.start();
        Bob.start();
        Catherine.start();

        try {
            Alice.join();
            Bob.join();
            Catherine.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        round.GreatestBalls();
    }
}