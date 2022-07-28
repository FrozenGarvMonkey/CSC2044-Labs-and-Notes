import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class PrintJob {
    
    static AtomicInteger numberWaitingJobs = new AtomicInteger(0);
    
    public static void main(String[] args){
        BlockingQueue printingJobQueue = new LinkedBlockingQueue();

        Computer computer = new Computer(printingJobQueue);

        MainPrinter mainPrinter = new MainPrinter(printingJobQueue);
        SubPrinter subPrinter = new SubPrinter(printingJobQueue);

        Thread computerAThread = new Thread(computer);
        Thread computerBThread = new Thread(computer);
        Thread computerCThread = new Thread(computer);
        Thread mainPrinterThread = new Thread(mainPrinter);
        Thread subPrinterThread = new Thread(subPrinter);
        
        computerAThread.setName("Computer A");
        computerBThread.setName("Computer B");
        computerCThread.setName("Computer C");

        computerAThread.start();
        computerBThread.start();
        computerCThread.start();
        mainPrinterThread.start();
        subPrinterThread.start();
    }
}

class Computer implements Runnable {
    BlockingQueue printingJobQueue;
    Random rand = new Random();

    Computer (BlockingQueue printingJobQueue){
        this.printingJobQueue = printingJobQueue;
    }

    @Override
    public void run(){
        for (int i =0; i<5; i++){
            PrintingJob printingJob = new PrintingJob (Thread.currentThread().getName(), String.valueOf(i));
            try {
                printingJobQueue.put(printingJob);
                PrintJob.numberWaitingJobs.incrementAndGet();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class MainPrinter implements Runnable {

    BlockingQueue printingJobQueue;

    MainPrinter(BlockingQueue printingJobQueue){this.printingJobQueue = printingJobQueue;}

    @Override
    public void run(){
        while(true){
            try{
                PrintingJob printingJob = (PrintingJob) printingJobQueue.take();
                PrintJob.numberWaitingJobs.decrementAndGet();
                System.out.println("Main Printer: " + printingJob.getJobName() + " from " + printingJob.getComputerName());
                Thread.sleep(2000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
class SubPrinter implements Runnable {

    BlockingQueue printingJobQueue;

    SubPrinter(BlockingQueue printingJobQueue){this.printingJobQueue = printingJobQueue;}

    @Override
    public void run(){
        while(true){
            if (PrintJob.numberWaitingJobs.get() > 5){
                try{
                    PrintingJob printingJob = (PrintingJob) printingJobQueue.take();
                    PrintJob.numberWaitingJobs.decrementAndGet();
                    System.out.println("Sub Printer: " + printingJob.getJobName() + " from " + printingJob.getComputerName());
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}

class PrintingJob{
    private String computerName;
    private String jobName;

    PrintingJob(String computerName, String jobName){
        this.computerName = computerName;
        this.jobName = jobName;
    }

    public String getComputerName() {
        return computerName;
    }

    public String getJobName() {
        return jobName;
    }
}