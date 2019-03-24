package History;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class History {
    private File file;

    public History(String directory,String name){
        File dir = new File(directory);
        if(!dir.exists()){
            if(dir.mkdir()){
                System.out.println("History directory created at " + dir);
            }else{
                System.out.println("Unable to create history directory at " + dir);
            }
        }
        file = new File(dir,name);
        if(!file.exists()){
            try {
                if(file.createNewFile()) {
                    System.out.println("History file created at " + name);
                }
            } catch (IOException e) {
                System.out.println("Unable to create history file at " + name);
            }
        }
    }

    public void writeHistory(String note){
        try{
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(note + "\n");
            bufferWriter.close();
        }catch (IOException e){
            System.out.println("Error while writing history to " + file.getPath());
        }
    }

    public String getHistory(){
        StringBuilder history = new StringBuilder();
        try {
            Files.lines(Paths.get(file.getName()), StandardCharsets.UTF_8).forEach(history::append);
        } catch (IOException e) {
            System.out.println("Error while reading history from " + file.getPath());
        }

        return history.toString();
    }
}
