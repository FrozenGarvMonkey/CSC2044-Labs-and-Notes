import java.util.concurrent.*;

class SumRow implements Runnable {
    private int[][] customerData;
    private int rowNumber;
    private int total = 0;

    SumRow(int[][] customerData, int rowNumber) {
        this.customerData = customerData;
        this.rowNumber = rowNumber;
    }

    @Override
    public void run() {
        // j - column index
        // j:0 -> Customer ID
        // j:13 -> Total Expenses
        // j loop from 1 to 12 (Jan to Dec)
        for(int j = 1; j < 13; j++) {
            total = total + customerData[rowNumber][j];
        }
        customerData[rowNumber][13] = total;
    }
}

public class Ex1 {
    public static void main(String[] args) {
        // Create an array with 10 rows and 14 columns.
        int[][] customerData = new int[10][14];
        int customerStartID = 100;
        int expenses = 1;
        // i - row index, j - column index
        // Just to fill the array with some values for calculation purpose.
        for(int i = 0; i < 10; i++) {
            customerData[i][0] = customerStartID;
            customerStartID++;
            for(int j = 1; j < 13; j++) {
                customerData[i][j] = expenses;
                expenses++;
            }
        }

        // Create a thread pool with 5 threads.
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Submit 10 tasks to the executor.
        // Each task is going to calculate the total expenses of one customer.
        for(int i = 0; i < 10; i++) {
            executor.submit((new SumRow(customerData,i)));
        }

        executor.shutdown();

        // Print out the last column (total expenses) to check the final answer.
        for(int i = 0; i < 10; i++) {
            System.out.println("Customer " + customerData[i][0] + " : " + customerData[i][13]);
        }

    }
}
