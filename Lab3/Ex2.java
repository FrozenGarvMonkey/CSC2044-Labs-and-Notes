package Lab3;

class ModifiedCounter extends Thread{
    private int valueOutsideBlock = 0;
    private int valueInsideBlock = 0;

    public void increaseCounterValueByOne() {
        valueOutsideBlock = valueOutsideBlock + 1;
        synchronized (this) {
            valueInsideBlock = valueInsideBlock + 1;
        }
    }

    public int getCounterValueOutsideBlock() {
        return valueOutsideBlock;
    }

    public int getCounterValueInsideBlock() {
        synchronized (this) {
            return valueInsideBlock;
        }
    }
}

public class Ex2 {
    public static void main(String[] args) {
        ModifiedCounter t1 = new ModifiedCounter();
        ModifiedCounter t2 = new ModifiedCounter();
        ModifiedCounter t3 = new ModifiedCounter();
        ModifiedCounter t4 = new ModifiedCounter();
        ModifiedCounter t5 = new ModifiedCounter();
        ModifiedCounter t6 = new ModifiedCounter();

        for (int i = 0;i<10000;i++) {
            t1.increaseCounterValueByOne();
            System.out.println(t1.getCounterValueOutsideBlock());
            System.out.println(t1.getCounterValueInsideBlock());
            t2.increaseCounterValueByOne();
            System.out.println(t2.getCounterValueOutsideBlock());
            System.out.println(t2.getCounterValueInsideBlock());
            t3.increaseCounterValueByOne();
            System.out.println(t3.getCounterValueOutsideBlock());
            System.out.println(t3.getCounterValueInsideBlock());
            t4.increaseCounterValueByOne();
            System.out.println(t4.getCounterValueOutsideBlock());
            System.out.println(t4.getCounterValueInsideBlock());
            t5.increaseCounterValueByOne();
            System.out.println(t5.getCounterValueOutsideBlock());
            System.out.println(t5.getCounterValueInsideBlock());
            t6.increaseCounterValueByOne();
            System.out.println(t6.getCounterValueOutsideBlock());
            System.out.println(t6.getCounterValueInsideBlock());
        }
    }
}