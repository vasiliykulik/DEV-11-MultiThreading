package main.java.locks;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Vasiliy Kylik on 13.05.2017.
 */
public class ReadWriteLocks {

  public static final int ARRAY_LENGTH = 10;

  public static void main(String[] args) {
    ConcurrentArray<Integer> array = new ConcurrentArray<>(ARRAY_LENGTH);
    IntStream.range(0, 3).forEach((i) -> new Thread(() -> {
      while (true) {
        System.out.println("Reading: " + Arrays.toString(array.read()));
      }
    }).start());
    new Thread(() -> {
      Random random = new Random();
      while (true) {
        /*Будем генерировать случайным образом различные массивы*/
        Integer[] ints = Stream
                /*передаем продьюсера интов*/
                .generate(random::nextInt)
                .limit(random.nextInt(ARRAY_LENGTH + 1))
                /*Что бы создать массив мы передаем ему ссылу на конструктор массивов*/
                .toArray(Integer[]::new);

       /*В стиле Java 7*/
/*       int size = random.nextInt(ARRAY_LENGTH+1);
       ints = new Integer[size];
              for(int i = 0; i<10; i++){
                ints[i] =  random.nextInt();
              }*/

        array.write(ints, ARRAY_LENGTH - ints.length);
      }
    }).start();
  }

  /*{jbv разбить читателей и писателей и раздать им различные локи */
  public static class ConcurrentArray<T> {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Object[] items;
    private Random random = new Random();

    public ConcurrentArray(int capacity) {
      items = new Object[capacity];
    }

    public void write(T[] values, int index) {
      readWriteLock.writeLock().lock();
      try {
        if (items.length < values.length + index) {
          throw new RuntimeException("Not enough space");
        }
      /*копирует из одного массива в другой, принимает 5 параметров*/
        System.arraycopy(values, 0, items, index, values.length);
        Thread.sleep(random.nextInt(1000));
        System.out.println("Array updated: " + Arrays.toString(items));
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      } finally {
        readWriteLock.writeLock().unlock();
      }
    }

    public T[] read() {
      /*Что бы код был потокобезопасным - мы не можем возвращать сами items,
      будем возвращать ссылку на массив*/
      readWriteLock.readLock().lock();
      try {
        Object[] result = Arrays.copyOf(items, items.length);
        Thread.sleep(random.nextInt(100));
        return (T[]) result;
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      } finally {
        readWriteLock.readLock().unlock();
      }
    }
  }
}
