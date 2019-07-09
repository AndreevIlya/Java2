package History;

import Message.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
        try {
            List<String> historyItems = getHistory();
            StringBuilder historyMessage = new StringBuilder();
            StringBuilder historyTime = new StringBuilder();
            int rows = 1;
            int index = 0;
            for (String item : historyItems) {
                Message message = new Message(item);
                if (index % 3 == 0) {
                    historyMessage.append(message.splitMessage());
                    rows = message.getRowsCount();
                } else if (index % 3 == 1) {
                    historyTime.append(message.formatTime(item, rows));
                }
                index++;
            }
            String[] historyOut = new String[2];
            historyOut[0] = historyMessage.toString();
            historyOut[1] = historyTime.toString();
            return historyOut;
        } catch (IOException e) {
            System.out.println("Error while reading history from " + file.getPath());
            e.printStackTrace();
        }
        return null;
    }

    private List<String> getHistory() throws IOException {
        List<String> historyLines = Files.readAllLines(Paths.get(file.getPath()));
        int count = historyLines.size();
        System.out.println("count" + count);
        if (count > LAST_ROWS_COUNT * 3) {
            return historyLines.subList(count - LAST_ROWS_COUNT * 3, count);
        }
        return historyLines;
    }
}
