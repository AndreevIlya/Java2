import java.util.Random;

public class Interrupted implements Runnable {
    private int counter = 0;
    private static Random rand = new Random();
    private static int randomNumber = rand.nextInt(100);

    @Override
    public void run(){
        while(counter < 110){
            counter++;
            if(counter % 10 == 0) System.out.println(counter);
            if(counter == randomNumber){
                Thread.currentThread().interrupt();
            }
            try {
                Thread.sleep(200);
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
