import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

class AccountWithExplicitLock {
    private int balance = 10000;
    private ReentrantLock lock = new ReentrantLock();

    public int getBalance() {
        return balance;
    }

    public void debit(int amount) {
        balance = balance - amount;
    }

    public void credit(int amount) {
        balance = balance + amount;
    }

    public ReentrantLock getLock() {
        return lock;
    }

}

class TransferHelperWithExplicitLock implements Runnable {
    private AccountWithExplicitLock[] accounts;
    private int numAccount, numIteration;
    private int fromAcctArrayIndex, toAcctArrayIndex;
    private int amount;
    private boolean transferSuccess = false;
    Random rand = new Random();

    TransferHelperWithExplicitLock(AccountWithExplicitLock[] accounts, int numAccount, int numIteration ) {
        this.accounts = accounts;
        this.numAccount = numAccount;
        this.numIteration = numIteration;
    }

    @Override
    public void run() {
        for(int i = 0; i < numIteration; i++) {
            fromAcctArrayIndex = rand.nextInt(numAccount);
            toAcctArrayIndex = rand.nextInt(numAccount);
            amount = rand.nextInt(10);
            System.out.println("Transfer $" + amount + " from Acct-" + fromAcctArrayIndex + " to " + toAcctArrayIndex);
            transferMoney(accounts[fromAcctArrayIndex], accounts[toAcctArrayIndex],	amount);
        }
    }

    public void transferMoney(AccountWithExplicitLock fromAcct, AccountWithExplicitLock toAcct, int amount) {
        while (transferSuccess == false) {
            if (fromAcct.getLock().tryLock()) { // success
                try {
                    if (toAcct.getLock().tryLock()) { // failed
                        try {
                            if (fromAcct.getBalance() < 0) {
                                System.out.println("Insufficient Funds");
                                System.exit(1);
                            } else {
                                fromAcct.debit(amount);
                                toAcct.credit(amount);
                                transferSuccess = true;
                            }
                        } finally {
                            toAcct.getLock().unlock();
                        }
                    }
                } finally {
                    fromAcct.getLock().unlock();
                }
            }
        }
    }
}

public class Ex1b {
    private static final int numThread = 20;
    private static final int numAccount = 5;
    private static final int numIteration = 1000000;

    public static void main(String[] args) {
        AccountWithExplicitLock[] accounts = new AccountWithExplicitLock[numAccount];
        for (int i = 0; i < numAccount; i++) {
            accounts[i] = new AccountWithExplicitLock();
        }
        for (int i = 0; i < numThread; i++) {
            new Thread(new TransferHelperWithExplicitLock(accounts,numAccount,numIteration)).start();
        }
    }
}
