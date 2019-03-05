import java.io.IOException;

public class MyClass implements AutoCloseable {
    @Override
    public void close() throws Exception {
        throw new Exception();
    }

    void read(int i) throws IOException{
        if(i == 1){
            System.out.println("Some symbols read.");
        }else {
            throw new IOException();
        }
    }
}
