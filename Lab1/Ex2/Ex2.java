package Lab1.Ex2;

class PrintNameAndValue implements Runnable {
    int iter;
    
    PrintNameAndValue(int y){
        iter = y;
    }

    public void run() {
        for (int x = 1; x <= iter; x++) {
            System.out.println("By " + Thread.currentThread().getName() + ", x is " + x);
        }
    }
}

public class Ex2 {
    public static void main(String[] args) {
        PrintNameAndValue pnav = new PrintNameAndValue(7);
        Thread t0 = new Thread(pnav);
        Thread t1 = new Thread(pnav);
        Thread t2 = new Thread(pnav);
        t0.start();
        t1.start();
        t2.start();
        System.out.println("This is the end of main() method.");
    }
}


/* 
a) Output is seemingly random, with the threads switching between the value of x accordingly. This could be due to them running concurrently and therefore output occurring on that basis.
*/