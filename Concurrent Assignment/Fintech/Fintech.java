package Fintech;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/* 
 * Class User :-
 * - Holds data for each row within the datasheet.
 */
class User{

    float share_price;
    String user_id;
    int	share_bought;
    String transaction_id;
    String stock_symbol;
    
    // Parses string data from file to equivalent data types.
    public User(String[] row_data) {
        this.share_price = Float.parseFloat(row_data[0]);
        this.user_id = row_data[1];
        this.share_bought = Integer.parseInt(row_data[2]);
        this.transaction_id = row_data[3];
        this.stock_symbol = row_data[4];
    }

    public String getUser_id() {
        return user_id;
    }

    // Returns the total cost spent for the specific object.
    public float getProduct() {
        return share_bought * share_price;
    }
    
}

/* 
 * Class UserCollection :-
 * - Holds data for each object for the collection of user data.
 * - A simplified version of the User class existing solely for summation.
 */
class UserCollection {
    String user_id;
    float total_spent = 0;

    
    public UserCollection(String user_id, float total_spent) {
        this.user_id = user_id;
        this.total_spent = total_spent;
    }
    
    public String getUser_id(){
        return user_id;
    }
    
    public float getTotal_spent() {
        return total_spent;
    }
    
    // Adds a value to the total spent for the specific user.
    public void AddToTotal(float amount){
        total_spent += amount;
    }
}

/* 
 * Class Calculator:- 
 * - Reads and parses the datasheet.
 * - Iterates over the parsed data and calculates the total amount spent on stocks for each user.
 * - All user_id fields are filtered and undergo summation of their total amount spent.
 */
class Calculator implements Runnable{
    // Start and End Cursor position for the thread.
    private int start_curr, end_curr; 
    // Collection of users
    public static ArrayList<UserCollection> UC = new ArrayList<>();

    public Calculator(int start_curr, int end_curr) {
        this.start_curr = start_curr;
        this.end_curr = end_curr;
    }

    // Parses the datasheet and returns it as an ArrayList.
    private static List<User> CSVParser (String file){
        List<User> transaction_history = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(file))) {
            br.readLine();
            String line = br.readLine();
            while (line != null) {
                String[] row_data = line.split(",");
                User user = new User(row_data);
                transaction_history.add(user);
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return transaction_history;
    }

    public void run(){
        // ArrayList containing all data from the datasheet.
        List<User> transaction_history = CSVParser("19073535.csv");
        
        for(int i = start_curr; i <= end_curr; i++){
            boolean foundInCollection = false;
            User row = transaction_history.get(i);

            /* 
             * Checks if the user_id of the current row matches with any of the
             * users in the User Collection. 
             * 
             * If it does, the total spent is simply added to the User within the Collection.
             * 
             * If it does not, the user_id is added to the User Collection along with the total_spent.
             */
            for(UserCollection u : UC){
                if(row.getUser_id().equals(u.getUser_id())){
                    u.AddToTotal(row.getProduct());
                    foundInCollection = true;
                }
            }
            
            if (foundInCollection == false){
                UserCollection tempUC = new UserCollection(row.getUser_id(), row.getProduct());
                UC.add(tempUC);
            }
        }
    }
}

/* 
 * Driver Class
 */
public class Fintech {
    public static void main(String[] args) throws InterruptedException {

        /* 
         * 4 Tasks Defined with each given 20000 fields to iterate through.
         * Splitting the file into 4 equal chunks speeds up the computational speed.
         */
        Calculator task1 = new Calculator(0,20000);
        Calculator task2 = new Calculator(20000,40000);
        Calculator task3 = new Calculator(40000,60000);
        Calculator task4 = new Calculator(60000,80455);

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);
        Thread t3 = new Thread(task3);
        Thread t4 = new Thread(task4);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // The User Collection is sorted by user_id.
        Calculator.UC.sort((u1,u2) -> u1.getUser_id().compareTo(u2.getUser_id()));
        // Output Formatting.
        System.out.println("user_id \t | \t total_money_spent");
        for (int i = 0; i < Calculator.UC.size(); i++){
            System.out.println(Calculator.UC.get(i).getUser_id() + " \t | \t " + String.format("%.2f", Calculator.UC.get(i).getTotal_spent()));
        }
    }
}
