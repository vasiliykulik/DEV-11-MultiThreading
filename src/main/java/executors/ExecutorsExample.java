package main.java.executors;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Vasiliy Kylik on 16.05.2017.
 */
public class ExecutorsExample {

  public static void main(String[] args) {
    new ExecutorsExample().testExecutor();
  }

  public void testExecutor() {
    Executor executor = Executors.newSingleThreadExecutor();
    executor.execute(new Runnable() {
      @Override
      public void run() {
        System.out.println("Async task started");
      }
    });
  }
}
