class BankAccount {
    private int mainBalance;
    private int subBalance;

    BankAccount(int mainBalance, int subBalance) {
        this.mainBalance = mainBalance;
        this.subBalance = subBalance;
    }

    public synchronized void mainDeposit(int mainAmount) {
        mainBalance = mainBalance + mainAmount;
    }

    public synchronized void mainWithdraw(int mainAmount) { mainBalance = mainBalance - mainAmount; }

    public synchronized void subDeposit(int subAmount) {
        subBalance = subBalance + subAmount;
    }

    public synchronized void subWithdraw(int subAmount) { subBalance = subBalance - subAmount; }

    public synchronized int getMainBalance() {
        return mainBalance;
    }

    public synchronized int getSubBalance() {
        return subBalance;
    }
}

class Parent implements Runnable {
    private BankAccount bankAccount;
    private int numberTransaction;
    private int mainAmount;
    private int subAmount;

    Parent(BankAccount bankAccount, int numberTransaction, int mainAmount, int subAmount) {
        this.bankAccount = bankAccount;
        this.numberTransaction = numberTransaction;
        this.mainAmount = mainAmount;
        this.subAmount = subAmount;
    }

    public void run() {
        for(int i = 0; i < numberTransaction; i++) {
            bankAccount.mainDeposit(mainAmount);
            bankAccount.subDeposit(subAmount);
        }
    }
}

public class MainSubTransaction {
    public static void main(String[] args) {
        BankAccount myBankAccount = new BankAccount(100,100);

        Thread fatherThread = new Thread(new Parent(myBankAccount,10,100,50));
        Thread motherThread = new Thread(new Parent(myBankAccount,10,100,50));

        fatherThread.start();
        motherThread.start();

        while(fatherThread.isAlive() || motherThread.isAlive()) { }

        System.out.println("Main Account Balance : " + myBankAccount.getMainBalance());
        System.out.println("Sub Account Balance : " + myBankAccount.getSubBalance());
    }
}
