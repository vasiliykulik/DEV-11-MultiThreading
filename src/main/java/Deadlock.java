package main.java;

/**
 * Created by Vasiliy Kylik on 12.05.2017.
 */
public class Deadlock {

  /*dead lock возникает случайно, поэтому нам нужно несколько раз  по разному вызвать transfer*/

  public static void main(String[] args) {
    new Thread()
  }

  public static void transfer(Account source, Account target, long amount) {
    synchronized (source) {
      synchronized (target) {
        if (source.getBalance() >= amount) {
          source.withdraw(amount);
          target.put(amount);
        }
      }
    }
  }

  public static class Worker implements Runnable{

    @Override
    public void run() {

    }
  }

  /*Представим себе банковский акаунт, напишем его представление в коде*/
  public static class Account {
    private long balance;

    public Account(long balance) {
      this.balance = balance;
    }

    public long getBalance() {
      return balance;
    }

    public void put(long amount) {
      balance += amount;
    }

    public void withdraw(long amount) {
      balance -= amount;
    }
  }
}
