package Lab3;

class Counter extends Thread{
    volatile private int value = 0;

    public void increaseCounterValueByOne() {
        value = value + 1;
    }

    public int getCounterValue() {
        return value;
    }
}

public class Ex1 {
    public static void main(String[] args) {
        Counter t1 = new Counter();
        Counter t2 = new Counter();
        Counter t3 = new Counter();
        Counter t4 = new Counter();
        Counter t5 = new Counter();
        Counter t6 = new Counter();

        for (int i = 0;i<10000;i++) {
            t1.increaseCounterValueByOne();
            System.out.println(t1.getCounterValue());
            t2.increaseCounterValueByOne();
            System.out.println(t2.getCounterValue());
            t3.increaseCounterValueByOne();
            System.out.println(t3.getCounterValue());
            t4.increaseCounterValueByOne();
            System.out.println(t4.getCounterValue());
            t5.increaseCounterValueByOne();
            System.out.println(t5.getCounterValue());
            t6.increaseCounterValueByOne();
            System.out.println(t6.getCounterValue());
        }
    }
}
