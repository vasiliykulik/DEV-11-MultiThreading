package main.java.executors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Created by Vasiliy Kylik on 16.05.2017.
 */
public class ExecutorsExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new ExecutorsExample().testScheduledAtFixedRate();
        /*new ExecutorsExample().testScheduled();*/
        /*new ExecutorsExample().testInvokeAll();*/
        /*new ExecutorsExample().testInvokeAny();*/
        /*new ExecutorsExample().testException();*/
    /*new ExecutorsExample().testSubmit();*/
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

    public void testException() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> f = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                throw new RuntimeException("Exception happened");
            }
        });
        System.out.println("Waiting for result");
        Thread.sleep(1000);
        try {
            System.out.println("result " + f.get());
        } catch (ExecutionException e) {
            System.out.println("Exception occurred");
        }
        executorService.shutdown();
    }

    public void testInvokeAny() throws ExecutionException, InterruptedException {
        List<Callable<String>> callables = new ArrayList<>();
        Random random = new Random();
        IntStream.range(0, 3).forEach(i -> callables.add(() -> {
            Thread.sleep(random.nextInt(1000));
            return String.valueOf(i);
        }));
        ExecutorService executor = Executors.newCachedThreadPool();
        String result = executor.invokeAny(callables);
        System.out.println(result);
        executor.shutdown();
    }

    public void testInvokeAll() throws ExecutionException, InterruptedException {
        List<Callable<String>> callables = new ArrayList<>();
        Random random = new Random();
        IntStream.range(0, 3).forEach(i -> callables.add(() -> {
            Thread.sleep(random.nextInt(1000));
            return String.valueOf(i);
        }));
        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<String>> result = executor.invokeAll(callables);
        for (Future f : result) {
            System.out.println(f.get());
        }
        /*result.forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        })*/
        ;
        executor.shutdown();
    }

    public void testScheduled() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        System.out.println("Task scheduled " + new Date());
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Task executed" + new Date());
            }
        }, 1, TimeUnit.SECONDS);
    }

    public void testScheduledAtFixedRate() throws InterruptedException {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        System.out.println("Task scheduled " + new Date());
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Task executed" + new Date());
            }
        }, 1, 1, TimeUnit.SECONDS);
        Thread.sleep(10000);
        executorService.shutdown();
    }
}