import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CalcAverageDriver{
    public static void main(String[] args){
        int [][] studentMarks = new int[6][6];

        Random rand = new Random();

        for(int i = 0; i<6; i++){
            studentMarks[i][0] = i+1;
            studentMarks[i][1] = rand.nextInt(26);
            studentMarks[i][2] = rand.nextInt(26);
        }

        CyclicBarrier barrier = new CyclicBarrier(5, new CalcAverage(studentMarks));

        Thread t0 = new Thread(new Worker(studentMarks,0,barrier));
        Thread t1 = new Thread(new Worker(studentMarks,1,barrier));
        Thread t2 = new Thread(new Worker(studentMarks,2,barrier));
        Thread t3 = new Thread(new Worker(studentMarks,3,barrier));
        Thread t4 = new Thread(new Worker(studentMarks,4,barrier));
        Thread t5 = new Thread(new Worker(studentMarks,5,barrier));

        t0.start();
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        for(int i = 0; i<6; i++){
            System.out.println("\n" + studentMarks[i][0] + " " + studentMarks[i][1] + " " + studentMarks[i][2] + " " + studentMarks[i][3]); 
        }

    }
}

class Worker implements Runnable{
    int[][] studentMarks;
    private int rowNumber;
    CyclicBarrier barrier;
    private int total;

    public Worker(int[][] studentMarks, int rowNumber, CyclicBarrier barrier) {
        this.studentMarks = studentMarks;
        this.rowNumber = rowNumber;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        total = studentMarks[rowNumber][1] + studentMarks[rowNumber][2];
        studentMarks[rowNumber][3] = total;

        try{
            barrier.await();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(BrokenBarrierException e){
            e.printStackTrace();
        }
    }
}

class CalcAverage implements Runnable {
    int[][] studentMarks;
    private double grandTotal, average;
    
    public CalcAverage(int[][] studentMarks) {
        this.studentMarks = studentMarks;
    }

    @Override
    public void run() {
        for (int i =0; i<6; i++){
            grandTotal = grandTotal + studentMarks[i][3];
        }
        average = grandTotal / 6;
        System.out.println("Average: " + average);
    }
}