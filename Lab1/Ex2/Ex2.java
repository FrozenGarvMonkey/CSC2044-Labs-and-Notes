package Lab1.Ex2;

class PrintNameAndValue implements Runnable {
    int iter;
    
    PrintNameAndValue(int y){
        iter = y;
    }

    public void run() {
        for (int x = 1; x <= iter; x++) {
            System.out.println("By " + Thread.currentThread().getName() + ", x is " + x);
            try{
                Thread.sleep(2000);
            }catch(InterruptedException e){System.out.println(e);}
        }
    }
}

public class Ex2 {
    public static void main(String[] args) {
        PrintNameAndValue pnav_1 = new PrintNameAndValue(7);
        PrintNameAndValue pnav_2 = new PrintNameAndValue(3);
        PrintNameAndValue pnav_3 = new PrintNameAndValue(10);

        Thread t0 = new Thread(pnav_1);
        Thread t1 = new Thread(pnav_2);
        Thread t2 = new Thread(pnav_3);
        t0.start();
        t1.start();
        t2.start();
        System.out.println("This is the end of main() method.");
    }
}


/* 
a) Output is seemingly random, with the threads switching between the value of x accordingly. This could be due to them running concurrently and therefore output occurring on that basis.
*/