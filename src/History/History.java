package History;

import Message.Message;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class History {
    private final File file;
    private static final int LAST_ROWS_COUNT = 10;

    public History(String rootDir, String directory, String name) {
        File dir;
        if (rootDir == null) {
            dir = new File(directory);
        } else {
            dir = new File(rootDir, directory);
        }
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
            System.out.println("Writing history: " + note + "\n");
            bufferWriter.write(note + "\n");
            bufferWriter.close();
        }catch (IOException e){
            System.out.println("Error while writing history to " + file.getPath());
        }
    }

    public String[] splitHistory() {
        String[] historyItems = getHistory().split("&");
        StringBuilder historyMessage = new StringBuilder();
        StringBuilder historyTime = new StringBuilder();
        int rows = 1;
        boolean index = true;
        for(String item : historyItems){
            Message message = new Message(item);
            if(index) {
                historyMessage.append(message.splitMessage());
                rows = message.getRowsCount();
            } else {
                historyTime.append(message.formatTime(item, rows));
            }
            index = !index;
        }
        String[] historyOut = new String[2];
        historyOut[0] = historyMessage.toString();
        historyOut[1] = historyTime.toString();

        return historyOut;
    }

    @NotNull
    private String getHistory() {
        StringBuilder history = new StringBuilder();
        try {
            String[] historyLines = Files.lines(Paths.get(file.getPath()), StandardCharsets.UTF_8).toArray(String[]::new);
            int count = historyLines.length;
            System.out.println("count" + count);
            int i = 1;
            if (count >= LAST_ROWS_COUNT) {
                for (int j = count - LAST_ROWS_COUNT; j < historyLines.length; j++) {
                    System.out.println("line " + (i++) + historyLines[j]);
                    history.append(historyLines[j]);
                }
            } else {
                for (String historyLine : historyLines) {
                    System.out.println("line " + (i++) + historyLine);
                    history.append(historyLine);
                }
            }
        } catch (IOException e) {
            System.out.println("Error while reading history from " + file.getPath());
            e.printStackTrace();
        }
        return history.toString();
    }
}
