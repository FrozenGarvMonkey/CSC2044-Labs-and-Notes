package Lab3;

import java.util.Random;

class BuffetLine {
    private int lobsterQuantity = 0;
    Random rand = new Random();

    public synchronized void addLobster() {
        if (lobsterQuantity > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lobsterQuantity = lobsterQuantity + rand.nextInt(10);
        System.out.println("Chef Refill " + lobsterQuantity + " Lobster(s)");
        notify();
    }

    public synchronized void getLobster() {
        if (lobsterQuantity == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int lobsterToGet = rand.nextInt(lobsterQuantity) + 1;
        lobsterQuantity = lobsterQuantity - lobsterToGet;
        System.out.println("Customer Took " + lobsterToGet + " Lobster(s)");
        notify();
    }
}

class Chef implements Runnable {
    BuffetLine buffetLine;

    Chef(BuffetLine buffetLine) {
        this.buffetLine = buffetLine;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            buffetLine.addLobster();
        }
    }
}

class Customer implements Runnable {
    BuffetLine buffetLine;

    Customer(BuffetLine buffetLine) {
        this.buffetLine = buffetLine;
    }

    public void run() {
        for (int i = 0; i < 10; i++) {
            buffetLine.getLobster();
        }
    }
}

public class Ex3 {
    public static void main(String[] args) {
        BuffetLine buffetLine = new BuffetLine();
        Thread chef = new Thread(new Chef(buffetLine));
        Thread customer = new Thread(new Customer(buffetLine));
        chef.start();
        customer.start();
    }
}