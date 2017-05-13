package main.java.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * Created by Vasiliy Kylik on 13.05.2017.
 */
public class Locks {
  private final Lock lock = new ReentrantLock(true);

  public static void main(String[] args) {
    System.out.println("START");
    Locks locks = new Locks();
    IntStream.range(0,10).forEach(i->new Thread(locks::testTryLock));
    System.out.println("END");
  }

  public void testLock() {
    /*Вынесли в переменную*/
    String threadName = Thread.currentThread().getName();
    System.out.println(threadName + " tried lock");
    lock.lock();
    try {
      System.out.println(threadName + " executing critical section");
      /*Имитация выполнения*/
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
      /*В случае InterruptedExeption что бы ничего не зависло, finally отпустим монитор*/
    } finally {
      System.out.println(threadName + " releasing lock");
      lock.unlock();
    }
  }

  public void testTryLock() {
    String threadName = Thread.currentThread().getName();
    System.out.println(threadName + " tries lock");
    try {
      if (lock.tryLock(100, TimeUnit.MILLISECONDS)) {
        try {
          System.out.println(threadName + " executing critical section");
          Thread.sleep(20);
        } finally {
          System.out.println(threadName + " releasing lock");
          lock.unlock();
        }
      } else {
        System.out.println(threadName + " Unable acquire lock");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
