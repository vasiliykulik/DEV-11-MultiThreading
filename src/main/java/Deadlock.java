package main.java;

import java.util.Random;

/**
 * Created by Vasiliy Kylik on 12.05.2017.
 */
public class Deadlock {
  /*dead lock возникает случайно, поэтому нам нужно несколько раз  по разному вызвать transfer*/


  private static Random random = new Random();
  private static Account account1 = new Account(1,100L);
  private static Account account2 = new Account(2,200L);

  public static void main(String[] args) {
    new Thread(new Worker()).start();
    new Thread(new Worker()).start();
  }

  public static void transfer(Account source, Account target, long amount) {
    Account a1 = source.compareTo(target)>0?source:target;
    Account a2 = source.compareTo(target)<0?source:target;
    synchronized (a1) {
      synchronized (a2) {
        if (source.getBalance() >= amount) {
          source.withdraw(amount);
          target.put(amount);
          System.out.println("Transfer: " + amount);
        } else {
          System.out.println("Not enough money");
        }
      }
    }
  }

  public static class Worker implements Runnable {

    @Override
    public void run() {
            /*Что бы  такая ситуация получилась - надо чтобы они друг на друга залокались и бесконечно ждут*/
      while (true) {
        if (random.nextBoolean()) {
          transfer(account1, account2, random.nextInt(10));
        } else {
          transfer(account2, account1, random.nextInt(10));
        }
      }
    }
  }

  /*Представим себе банковский акаунт, напишем его представление в коде*/
  public static class Account implements Comparable<Account> {
    private long balance;
    private int id;

    public Account(int id, long balance) {
      this.id = id;
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

    @Override
    public int compareTo(Account o) {
      return id - o.id;// заменим ifelse вычитанием
    }
  }
}
