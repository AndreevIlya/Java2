package Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Message {
    private final String message;
    private static final int WIDTH = 45;
    private int rowsCount = 1;

    public Message(String message){
        this.message = message;
    }

    public String splitMessage(){
        StringBuilder splitMessage = new StringBuilder();
        int lineLength = message.length();
        int wordsLength = 0;
        if(lineLength >= WIDTH){
            String[] words = message.split(" ");
            for(String word : words){
                wordsLength += word.length() + 1;
                if(wordsLength > WIDTH){
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

    private String getTime(){
        Date time = Calendar.getInstance().getTime();
        return new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(time);
    }

    public String formatTime(){
        StringBuilder time = new StringBuilder(getTime());
        for(int i = 0; i < rowsCount; i++){
            time.append("\n");
        }
        return time.toString();
    }

    public String formatTime(String timeStr, int rows){
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
