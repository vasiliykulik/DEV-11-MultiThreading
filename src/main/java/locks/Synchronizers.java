package main.java.locks;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.stream.IntStream;

/**
 * Created by Vasiliy Kylik on 14.05.2017.
 */

public class Synchronizers {

  public static void main(String[] args) throws InterruptedException {
    Synchronizers synchronizers = new Synchronizers();
    synchronizers.testExchanger();
  }

  public void testCyclicBarrier() throws InterruptedException {
    /*не надо пересоздавать каждый раз*/
    CyclicBarrier barrier = new CyclicBarrier(5, () -> System.out.println("Barrier exceeded"));
    while (true) {
      new Thread(() -> {
        /*Подвесит наши потоки*/
        try {
          String threadName = Thread.currentThread().getName();
          System.out.println(threadName + " starts waiting on barrier");
          barrier.await();
          System.out.println(threadName + " finish waiting");
/*Барьер будет сломлен если один из потоков, не дождавшись таймаута - выпадет с InterruptedException */
        } catch (InterruptedException | BrokenBarrierException e) {
          e.printStackTrace();
        }
      }).start();
      Thread.sleep(1000);
    }
  }

  public void testExchanger() {

    Exchanger<String> exchanger = new Exchanger<>();
    IntStream.range(0, 2).forEach((i) -> new Thread(() -> {
      try {
        System.out.printf("");
        Thread.sleep(new Random().nextInt(1000));
        String name = Thread.currentThread().getName();
        System.out.println(name + " ready to exchange");
        System.out.println(name + " < - > " + exchanger.exchange(name));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start());
  }
}
