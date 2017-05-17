package main.java.executors;

import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Created by Vasiliy Kylik on 5/18/2017.
 */
public class ExecutorServiceSubmit {

    public static void main(String[] args) {
        new ExecutorServiceSubmit().testExchanger();
    }

    public void test() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> f = executorService.submit((Callable<String>) () -> {
            throw new RuntimeException("Exception happened");
        });
        try {
            System.out.println("result: " + f.get());
        } catch (Exception e) {
        }
        executorService.shutdown();
    }

    public void testFixedRate() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        System.out.println("Task scheduled");
        executorService.scheduleAtFixedRate(() -> System.out.println("Task executed"), 1, 1, TimeUnit.SECONDS);
    }

    public void testExchanger() {
        Exchanger<String> exchanger = new Exchanger<>();
        Random random = new Random();
        IntStream.range(0, 2).forEach((i) -> new Thread(() -> {
            try {
                Thread.sleep(random.nextInt(1000));
                String name = Thread.currentThread().getName();
                System.out.println(name + " ready to exchange");
                System.out.println(name + " < - > " + exchanger.exchange(name));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }
}
