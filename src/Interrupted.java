/*
* Sometimes during sleep exception is not thrown.
* */

import java.util.Random;

public class Interrupted implements Runnable {
    private int counter = 0;
    private Random rand = new Random();

    @Override
    public void run(){
        while(counter < 1100){
            counter++;
            if(counter % 100 == 0) System.out.println(counter);
            if(counter == rand.nextInt(1000)){
                Thread.currentThread().interrupt();
            }
            try {
                Thread.sleep(20);
            }catch (InterruptedException exc){
                System.out.println("Thread is interrupted at " + counter);
                return;
            }
        }
        System.out.println("Passed w/o exception...");
    }

    public static void main(String[] args){
        Interrupted interrupted = new Interrupted();
        Thread thread = new Thread(interrupted);

        thread.start();

    }
}
