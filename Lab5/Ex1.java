import java.util.Random;

class Account {
    private int balance = 10000;

    public int getBalance() {
        return balance;
    }

    public void debit(int amount) {
        balance = balance - amount;
    }

    public void credit(int amount) {
        balance = balance + amount;
    }
}

class TransferHelper implements Runnable {
    private Account[] accounts;
    private int numAccount, numIteration;
    private int fromAcctArrayIndex, toAcctArrayIndex;
    private int amount;
    Random rand = new Random();

    TransferHelper(Account[] accounts, int numAccount, int numIteration ) {
        this.accounts = accounts;
        this.numAccount = numAccount;
        this.numIteration = numIteration;
    }

    @Override
    public void run() {
        for(int i = 0; i < numIteration; i++) {
            // Randomly select an account.
            fromAcctArrayIndex = rand.nextInt(numAccount);
            // Randomly select another account.
            toAcctArrayIndex = rand.nextInt(numAccount);
            amount = rand.nextInt(10);
            System.out.println("Transfer $" + amount + " from Acct-" +
                    fromAcctArrayIndex +" to " + toAcctArrayIndex);
            transferMoney(accounts[fromAcctArrayIndex], accounts[toAcctArrayIndex],	amount);
        }
    }

    public void transferMoney(Account fromAcct, Account toAcct, int amount) {
        synchronized(fromAcct) {
            synchronized (toAcct) {
                if (fromAcct.getBalance() < 0) {
                    System.out.println("Insufficient Funds");
                    System.exit(1);
                } else {
                    fromAcct.debit(amount);
                    toAcct.credit(amount);
                }
            }
        }
    }
}

public class Ex1 {
    private static final int numThread = 20;
    private static final int numAccount = 5;
    private static final int numIteration = 1000000;

    public static void main(String[] args) {
        Account[] accounts = new Account[numAccount];
        for (int i = 0; i < numAccount; i++) {
            accounts[i] = new Account();
        }
        for (int i = 0; i < numThread; i++) {
            new Thread(new TransferHelper(accounts,numAccount,numIteration)).start();
        }
    }
}
