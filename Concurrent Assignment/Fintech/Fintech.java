package Fintech;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class User{

    float share_price;
    String user_id;
    int	share_bought;
    String transaction_id;
    String stock_symbol;
    
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

    public float getProduct() {
        return share_bought * share_price;
    }
    
}

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
    
    public void AddToTotal(float amount){
        total_spent += amount;
    }
}

class Calculator implements Runnable{
    private int start_curr, end_curr;
    public static ArrayList<UserCollection> UC = new ArrayList<>();

    public Calculator(int start_curr, int end_curr) {
        this.start_curr = start_curr;
        this.end_curr = end_curr;
    }

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
        List<User> transaction_history = CSVParser("19073535.csv");
        
        for(int i = start_curr; i <= end_curr; i++){
            boolean foundInCollection = false;
            User row = transaction_history.get(i);

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


public class Fintech {
    public static void main(String[] args) throws InterruptedException {
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

        Calculator.UC.sort((u1,u2) -> u1.getUser_id().compareTo(u2.getUser_id()));
        System.out.println("user_id \t | \t total_money_spent");
        for (int i = 0; i < Calculator.UC.size(); i++){
            System.out.println(Calculator.UC.get(i).getUser_id() + " \t | \t " + String.format("%.2f", Calculator.UC.get(i).getTotal_spent()));
        }
    }
}
