package main.java;

/**
 * Created by Vasiliy Kylik on 13.05.2017.
 */
public class VolatileExample {

  private volatile static boolean flag=true;

  public static void main(String[] args) throws InterruptedException {
    new Thread(new Worker()).start();
    Thread.sleep(100); // Усыпляем наш, текущий поток на 100 мсек
    flag =  false;
    System.out.println(flag);
  }

  public static class Worker implements Runnable{
    @Override
    public void run() {
      int i =0;
      while (flag){
        i++;
      }
     /*При оптимизации кода может превратится в */
     /*if(flag){
       int i=0;
       while (true){
         i++;
       }
     }*/
      System.out.println(i);
    }
  }
}
