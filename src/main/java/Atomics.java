package main.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Vasiliy Kylik on 15.05.2017.
 */
public class Atomics {
  private static AtomicInteger counter = new AtomicInteger(0);

  public static void main(String[] args) throws InterruptedException {
    new Atomics().test();
  }

  public static int increment() {
      return counter.getAndIncrement();
    }


  public void test() throws InterruptedException {
    List<Aggregator> aggregators = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Aggregator aggregator = new Aggregator();
      aggregators.add(aggregator);
      new Thread(aggregator).start();
    }
    Thread.sleep(100);
    boolean isValid = true;
    Set<Integer> integerSet = new HashSet<>();
    for (Aggregator aggregator : aggregators) {

      for (Integer anInt : aggregator.ints) {
        if (!integerSet.add(anInt)) {
          System.out.println("Error, duplicate found" + anInt);
          isValid = false;
        }
      }
    }
    if (isValid) {
      System.out.println("No duplicates");
    }
  }

  public class Aggregator implements Runnable {

    private List<Integer> ints = new ArrayList<>();

    @Override
    public void run() {
      for (int i = 0; i < 1000; i++) {
        ints.add(increment());
      }
    }
  }
}
