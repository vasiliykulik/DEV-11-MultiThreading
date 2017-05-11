package main.java;

/**
 * Created by Vasiliy Kylik on 11.05.2017.
 */
public class CountDownLatch {
  private int counter = 0;
  private final Object lock = new Object();

  public CountDownLatch(int counter) {
    this.counter = counter;
  }

  public void await() throws InterruptedException {

      synchronized (lock) {
        if (counter > 0) {
          lock.wait();
        }
      }
    }

  public void countDown() {
    synchronized (lock) {
      if (counter > 0) {
        counter--;
        System.out.println("Counter = " + counter);
      }
      if(counter==0){
        lock.notifyAll();
      }
    }
  }

  public int getCounter() {
    synchronized (lock){
      return counter;
    }
  }
}
