import java.util.concurrent.*;

class FindLargestValue implements Callable<Integer> {
    private int[][] intArray;
    private int startRow, startCol, length;
    private int localLargestValue = -1;

    FindLargestValue(int[][] intArray, int startRow, int startCol, int length) {
        this.intArray = intArray;
        this.startRow = startRow;
        this.startCol = startCol;
        this.length = length;
    }

    public Integer call() throws Exception {
        // i - row index, j - column index
        // Assume we are finding the largest value in the top left quadrant.
        // startRow = 0, startCol = 0, length = 4

        // Assume we are finding the largest value in the top right quadrant.
        // startRow = 0, startCol = 4, length = 4
        for(int i = startRow; i < (startRow+length); i++) {
            for(int j = startCol; j < (startCol+length); j++) {
                if (intArray[i][j] > localLargestValue) {
                    localLargestValue = intArray[i][j];
                }
            }
        }
        return localLargestValue;
    }
}

public class Ex2 {
    public static void main(String[] args) {
        // Create an array with 8 rows and 8 columns.
        int[][] intArray = new int[8][8];
        int countValue = 1;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                intArray[i][j] = countValue;
                countValue++;
            }
        }

        // Create a thread pool with 4 threads.
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Create 4 callable tasks (each for a quadrant).
        FindLargestValue topLeftQuadrant = new FindLargestValue(intArray,0,0,4);
        FindLargestValue topRightQuadrant = new FindLargestValue(intArray,0,4,4);
        FindLargestValue bottomLeftQuadrant = new FindLargestValue(intArray,4,0,4);
        FindLargestValue bottomRightQuadrant = new FindLargestValue(intArray,4,4,4);

        // Submit the tasks to the executor.
        Future<Integer> futureOfTopLeftQuadrant = executor.submit(topLeftQuadrant);
        Future<Integer> futureOfTopRightQuadrant = executor.submit(topRightQuadrant);
        Future<Integer> futureOfBottomLeftQuadrant = executor.submit(bottomLeftQuadrant);
        Future<Integer> futureOfBottomRightQuadrant = executor.submit(bottomRightQuadrant);

        int topLeftLargest = 0;
        int topRightLargest = 0;
        int bottomLeftLargest = 0;
        int bottomRightLargest = 0;

        // Retrieve the largest value from each thread.
        try {
            topLeftLargest = futureOfTopLeftQuadrant.get();
            topRightLargest = futureOfTopRightQuadrant.get();
            bottomLeftLargest = futureOfBottomLeftQuadrant.get();
            bottomRightLargest = futureOfBottomRightQuadrant.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Compare the four return values.
        if (topLeftLargest >= topRightLargest && topLeftLargest >= bottomLeftLargest && topLeftLargest >= bottomRightLargest) {
            System.out.println("Largest Value = " + topLeftLargest);
        } else if (topRightLargest >= topLeftLargest && topRightLargest >= bottomLeftLargest && topRightLargest >= bottomRightLargest) {
            System.out.println("Largest Value = " + topRightLargest);
        } else if (bottomLeftLargest >= topLeftLargest && bottomLeftLargest >= topRightLargest && bottomLeftLargest >= bottomRightLargest) {
            System.out.println("Largest Value = " + bottomLeftLargest);
        } else {
            System.out.println("Largest Value = " + bottomRightLargest);
        }

        executor.shutdown();
    }
}
