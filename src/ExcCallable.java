import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class ExcCallable implements Callable {
    @Override
    public Object call() throws Exception{
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if((new Random()).nextInt(10) == 3) throw new Exception();
        return Thread.currentThread().getName();
    }

    public static void main(String[] a){
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<String>> list = new ArrayList<>();
        Callable callable = new ExcCallable();
        for (int i = 0; i < 100; i++) {
            Future<String> future = null;
            try {
                future = executor.submit(callable);
            }catch (Exception e){
                System.out.println("Exception.");
            }
            list.add(future);
        }
        for(Future<String> future : list){
            try {
                System.out.println(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

    }
}
