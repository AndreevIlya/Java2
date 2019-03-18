import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class ChatClient extends JFrame {
    private JButton buttonEnter = createSendButton();
    private JTextField textField = createTextField();
    private JTextArea textArea = createTextArea();
    private JTextArea timeArea = createTextArea();
    private JPanel pane = createPane();
    private JPanel innerPane = createInnerPane();

    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;


    ChatClient(){
        initConnection();
        initGUI();
        initReceiver();
    }

    private void initGUI(){
        drawWindow();
        setElements();
        addListeners();
        setVisible(true);
    }

    private void initConnection() {
        try {
            socket = new Socket("localhost", 8080);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            System.out.println("Connection initialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initReceiver() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    String messageFromServer = inputStream.readUTF();
                    if(!messageFromServer.equals("")){
                        textArea.append("Server: ");
                        Message readyMessage = new Message(messageFromServer);
                        readyMessage.writeMessage();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        System.out.println("receiver started");
    }

    private void drawWindow() {
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)(screenSize.getWidth() - 1000) / 2;
        int y = (int)(screenSize.getHeight() - 800) / 2;
        if(x < 0) x = 0;
        if(y < 0) y = 0;
        setBounds(x,y,1000,800);
        setTitle("Чат");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setElements(){
        add(buttonEnter,BorderLayout.SOUTH);
        add(pane,BorderLayout.CENTER);
        pane.add(textField,BorderLayout.SOUTH);
        pane.add(innerPane,BorderLayout.CENTER);
        innerPane.add(new JScrollPane(textArea));
        innerPane.add(new JScrollPane(timeArea));
    }

    private JPanel createPane(){
        JPanel pane = new JPanel();
        pane.setBackground(new Color(0xcccccc));
        pane.setLayout(new BorderLayout());

        return pane;
    }

    private JPanel createInnerPane(){
        JPanel pane = new JPanel();
        pane.setBackground(new Color(0xcccccc));
        pane.setLayout(new GridLayout(1,2));

        return pane;
    }

    private JButton createSendButton(){
        JButton buttonEnter = new JButton("Enter");
        buttonEnter.setHorizontalAlignment(SwingConstants.CENTER);
        buttonEnter.setVerticalAlignment(SwingConstants.CENTER);
        buttonEnter.setBackground(new Color(0xd0d0d0));
        buttonEnter.setOpaque(true);
        buttonEnter.setFocusPainted(false);
        buttonEnter.setFont(new Font("Verdana",Font.ITALIC,26));
        buttonEnter.setForeground(new Color(0x111));

        return buttonEnter;
    }

    private JTextField createTextField(){
        JTextField textField = new JTextField();
        textField.setHorizontalAlignment(SwingConstants.LEFT);
        textField.setMargin(new Insets(20,50,20,50));
        textField.setBackground(new Color(0xeeeeee));
        textField.setOpaque(true);
        textField.setFont(new Font("Verdana",Font.ITALIC,18));
        textField.setForeground(new Color(0x444444));

        return textField;
    }

    private JTextArea createTextArea(){
        JTextArea textArea = new JTextArea();
        textArea.setMargin(new Insets(20,20,20,20));
        textArea.setFont(new Font("Verdana",Font.PLAIN,18));
        textArea.setEditable(false);

        return textArea;
    }

    private class Message{
        private String message;
        private final int width = 45;
        private int rowsCount = 1;

        Message(String message){
            this.message = message;
        }

        private String splitMessage(){
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

        private int getRowsCount(){
            return rowsCount;
        }

        private String getTime(){
            StringBuilder time = new StringBuilder(new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(Calendar.getInstance().getTime()));
            for(int i = 0; i < getRowsCount(); i++){
                time.append("\n");
            }
            return time.toString();
        }

        private void writeMessage(){
            textArea.append(splitMessage());
            timeArea.append(getTime());
        }
    }

    private void processMessage(String message) {
        if(!message.equals("")) {
            textField.setText("");
            textArea.append("You: ");
            Message readyMessage = new Message(message);
            readyMessage.writeMessage();
            sendMessage(message);
        }
    }

    private void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClickButton(){
        buttonEnter.addActionListener(e -> {
                processMessage(textField.getText());
                textField.requestFocus();
            }
        );
    }

    private void handlePressEnter(){
        textField.addActionListener(e -> processMessage(textField.getText()));
    }

    private void addListeners(){
        addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                textField.requestFocusInWindow();
            }
        });
        handlePressEnter();
        handleClickButton();
    }
}
