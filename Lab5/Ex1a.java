import java.util.Random;

class AccountWithAcctNumber {
    private int balance = 10000;
    private int acctNumber;

    AccountWithAcctNumber(int acctNumber) {
        this.acctNumber = acctNumber;
    }

    public int getAcctNumber() {
        return acctNumber;
    }

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

class TransferHelperWithAcctNumber implements Runnable {
    private AccountWithAcctNumber[] accounts;
    private int numAccount, numIteration;
    private int fromAcctNumber, fromAcctArrayIndex;
    private int toAcctNumber, toAcctArrayIndex;
    private int amount;
    private AccountWithAcctNumber fromAcct, toAcct;
    Random rand = new Random();

    TransferHelperWithAcctNumber(AccountWithAcctNumber[] accounts, int numAccount, int numIteration ) {
        this.accounts = accounts;
        this.numAccount = numAccount;
        this.numIteration = numIteration;
    }

    @Override
    public void run() {
        for(int i = 0; i < numIteration; i++) {
            // Randomly select an account.
            fromAcctArrayIndex = rand.nextInt(numAccount);
            fromAcct = accounts[fromAcctArrayIndex];
            fromAcctNumber = fromAcct.getAcctNumber();
            // Randomly select another account.
            toAcctArrayIndex = rand.nextInt(numAccount);
            toAcct = accounts[toAcctArrayIndex];
            toAcctNumber = toAcct.getAcctNumber();
            amount = rand.nextInt(10);
            System.out.println("Transfer $" + amount + " from Acct-" + fromAcctNumber + " to " + toAcctNumber);
            transferMoney(fromAcct, toAcct,	amount, fromAcctNumber, toAcctNumber);
        }
    }

    public void transferMoney(AccountWithAcctNumber fromAcct, AccountWithAcctNumber toAcct, int amount, int fromAcctNumber, int toAcctNumber) {
        if (fromAcctNumber > toAcctNumber) {
            synchronized (fromAcct) {
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
        } else {
            synchronized (toAcct) {
                synchronized (fromAcct) {
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
}

public class Ex1a {
    private static final int numThread = 20;
    private static final int numAccount = 5;
    private static final int numIteration = 1000000;

    public static void main(String[] args) {
        AccountWithAcctNumber[] accounts = new AccountWithAcctNumber[numAccount];
        for (int i = 0; i < numAccount; i++) {
            accounts[i] = new AccountWithAcctNumber(i);
        }
        for (int i = 0; i < numThread; i++) {
            new Thread(new TransferHelperWithAcctNumber(accounts,numAccount,numIteration)).start();
        }
    }
}
