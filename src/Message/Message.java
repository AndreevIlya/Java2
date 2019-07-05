package Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Message {
    private String message;
    private final int width = 45;
    private int rowsCount = 1;

    public Message(String message){
        this.message = message;
    }

    public String splitMessage(){
        StringBuilder splitMessage = new StringBuilder();
        int lineLength = message.length();
        int wordsLength = 0;
        if(lineLength >= width){
            String[] words = message.split(" ");
            for(String word : words){
                wordsLength += word.length() + 1;
                if(wordsLength > width){
                    splitMessage.append("\n").append(word).append(" ");
                    wordsLength = word.length() + 1;
                    rowsCount++;
                }else{
                    splitMessage.append(word).append(" ");
                }
            }
            splitMessage.append("\n");
            return splitMessage.toString();
        }else{
            return message + "\n";
        }
    }

    public String getTime(){
        StringBuilder time = new StringBuilder(new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(Calendar.getInstance().getTime()));
        for(int i = 0; i < rowsCount; i++){
            time.append("\n");
        }
        return time.toString();
    }

    public String getTime(String timeStr, int rows){
        StringBuilder time = new StringBuilder(timeStr);
        for(int i = 0; i < rows; i++){
            time.append("\n");
        }
        return time.toString();
    }

    public int getRowsCount(){
        return rowsCount;
    }
}
