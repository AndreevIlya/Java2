package Client;

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
    private JButton enterButton = createSendButton("Enter");
    private JButton loginButton = createSendButton("Log in");
    private JTextField textField = createTextField();
    private JTextField loginField = createTextField();
    private JTextField passField = createTextField();
    private JTextArea textArea = createTextArea();
    private JTextArea timeArea = createTextArea();
    private JLabel loggingLabel = createLabel();
    private JPanel loginPane = createColumnPane(3);


    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    public static void main(String[] args) {
        new ChatClient();
    }

    private ChatClient(){
        initGUI();

        initConnection();
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
        /*Thread loginThread = new Thread(() -> {
            while (true) {
                System.out.println(1);
                try {
                    String messageFromServer = inputStream.readUTF();
                    System.out.println(4 + messageFromServer);
                    if(!messageFromServer.equals("Failed.")){
                        loggingLabel.setText("Login is already occupied or password is wrong.");
                        loggingLabel.setVisible(true);
                    }else if(messageFromServer.equals(loginField.getText())){
                        loggingLabel.setText("You are logged in as " + messageFromServer + ".");
                        loggingLabel.setVisible(true);
                        enterButton.setVisible(true);
                        textField.setVisible(true);
                        loginPane.setVisible(false);
                        break;
                    }else{
                        System.out.println("2" + messageFromServer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        loginThread.setDaemon(true);
        loginThread.start();*/
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    String messageFromServer = inputStream.readUTF();
                    System.out.println(5 + messageFromServer);
                    if(!messageFromServer.equals("Failed.")){
                        loggingLabel.setText("Login is already occupied or password is wrong.");
                        loggingLabel.setVisible(true);
                    }else if(messageFromServer.equals(loginField.getText())){
                        loggingLabel.setText("You are logged in as " + messageFromServer + ".");
                        loggingLabel.setVisible(true);
                        enterButton.setVisible(true);
                        textField.setVisible(true);
                        loginPane.setVisible(false);
                        break;
                    }else{
                        System.out.println("2" + messageFromServer);
                    }
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
        JPanel pane = createPane();
        JPanel innerPane = createColumnPane(2);
        pane.add(loggingLabel,BorderLayout.NORTH);
        loggingLabel.setVisible(false);
        add(loginPane,BorderLayout.NORTH);
        add(enterButton,BorderLayout.SOUTH);
        enterButton.setVisible(false);
        add(pane,BorderLayout.CENTER);
        pane.add(textField,BorderLayout.SOUTH);
        textField.setVisible(false);
        pane.add(innerPane,BorderLayout.CENTER);
        innerPane.add(new JScrollPane(textArea));
        innerPane.add(new JScrollPane(timeArea));
        loginPane.add(loginField);
        loginPane.add(passField);
        loginPane.add(loginButton);
    }

    private JPanel createPane(){
        JPanel pane = new JPanel();
        pane.setBackground(new Color(0xcccccc));
        pane.setLayout(new BorderLayout());

        return pane;
    }

    private JPanel createColumnPane(int columns){
        JPanel pane = new JPanel();
        pane.setBackground(new Color(0xcccccc));
        pane.setLayout(new GridLayout(1,columns));

        return pane;
    }

    private JButton createSendButton(String name){
        JButton buttonEnter = new JButton(name);
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

    private JLabel createLabel(){
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBackground(new Color(0xeeeeee));
        label.setOpaque(true);
        label.setFont(new Font("Verdana",Font.ITALIC,18));
        label.setForeground(new Color(0x444444));

        return label;
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

        private String getTime(){
            StringBuilder time = new StringBuilder(new SimpleDateFormat("HH:mm:ss dd.MM.yyyy").format(Calendar.getInstance().getTime()));
            for(int i = 0; i < rowsCount; i++){
                time.append("\n");
            }
            return time.toString();
        }

        private void writeMessage(){
            textArea.append(splitMessage());
            timeArea.append(getTime());
        }
    }

    private void putMessage(String message) {
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

    private void initializeLogin(){
        String login = loginField.getText();
        String password = passField.getText();
        if(!login.equals("") && !password.equals("")){
            sendMessage(login + "&" + password);
        }
    }

    private void handleLogin(){
        loginButton.addActionListener(e->initializeLogin());
        loginField.addActionListener(e->initializeLogin());
        passField.addActionListener(e->initializeLogin());
    }

    private void handleClickButton(){
        enterButton.addActionListener(e -> {
                putMessage(textField.getText());
                textField.requestFocus();
            }
        );
    }

    private void handlePressEnter(){
        textField.addActionListener(e -> putMessage(textField.getText()));
    }

    private void addListeners(){
        addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                textField.requestFocusInWindow();
            }
        });
        handlePressEnter();
        handleClickButton();
        handleLogin();
    }
}
