package main.java.executors;

import java.util.concurrent.*;

/**
 * Created by Vasiliy Kylik on 16.05.2017.
 */
public class ExecutorsExample {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    new ExecutorsExample().testSubmit();
    /*new ExecutorsExample().testExecutor();*/
  }

  public void testExecutor() {
    Executor executor = Executors.newSingleThreadExecutor();
    System.out.println(Thread.currentThread().getName() + " submits task");
    executor.execute(new Runnable() {
      @Override
      public void run() {
        System.out.println(Thread.currentThread().getName() + " Async task started");
      }
    });
  }

  public void testSubmit() throws ExecutionException, InterruptedException {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    Future<String> f = executorService.submit(new Callable<String>() {
      @Override
      public String call() throws Exception {
        Thread.sleep(1000);
        return "Task executed";
      }
    });
    System.out.println("Waiting for result");
    System.out.println("result " + f.get());
    f.get();
    executorService.shutdown();
  }
}
