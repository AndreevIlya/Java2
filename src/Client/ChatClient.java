package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class ChatClient extends JFrame {
    private JButton enterButton = createSendButton("Enter");
    private JButton loginButton = createSendButton("Log in");
    private JButton logoutButton = createSendButton("Log out");
    private JTextField textField = createTextField();
    private JTextField loginField = createTextField();
    private JTextField passField = createTextField();
    private JTextArea textArea = createTextArea();
    private JTextArea timeArea = createTextArea();
    private JPanel pane = createPane();
    private JPanel innerPane = createColumnPane(2);
    private JPanel loginPane = createColumnPane(3);
    private JPanel loggedPane = createPane();
    private JLabel loggingFailureLabel = createLabel();
    private JLabel loggingSuccessLabel = createLabel();

    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    ChatClient(){
        drawWindow();
        setElementsInit();
        addListeners();
        setVisible(true);
    }

    private void initConnection() {
        try {
            socket = new Socket("192.168.1.103", 4444);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            System.out.println("Connection initialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleInput(){
        if (!socket.isClosed()) {
            try {
                String[] messageFromServer = inputStream.readUTF().split("&");
                switch (messageFromServer[0]) {
                    case "fail":
                        System.out.println("failed to login");
                        addLogFailureLabel("Login is already occupied and has another password.");
                        break;
                    case "occupied":
                        System.out.println(loginField.getText() + " is already logged in.");
                        addLogFailureLabel(loginField.getText() + " is already logged in.");
                        break;
                    case "logged":
                        addLoggedElements(loginField.getText());
                        System.out.println("Logged in.");
                        break;
                    case "logout":
                        addLoginPane();
                        System.out.println("Logged in.");
                        break;
                    case "message":
                        System.out.println("got " + messageFromServer[1]);
                        putMessage(messageFromServer[1], messageFromServer[2]);
                        break;
                }
            } catch(EOFException exc){
                System.out.println("Full halt.");
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initReceiver(){
        Thread thread = new Thread(() -> {
            while (true) {
                handleInput();
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

    private void setElementsInit(){
        add(loginPane,BorderLayout.NORTH);
        loginPane.add(loginField);
        loginPane.add(passField);
        loginPane.add(loginButton);
        add(pane,BorderLayout.CENTER);
        pane.add(innerPane,BorderLayout.CENTER);
        innerPane.add(new JScrollPane(textArea));
        innerPane.add(new JScrollPane(timeArea));
    }

    private void addLogFailureLabel(String message){
        pane.removeAll();
        add(loginPane,BorderLayout.NORTH);
        loginPane.add(loginField);
        loginPane.add(passField);
        loginPane.add(loginButton);
        loggingFailureLabel.setText(message);
        pane.add(loggingFailureLabel,BorderLayout.NORTH);
        pane.add(innerPane,BorderLayout.CENTER);
        setVisible(true);
    }

    private void addLoginPane(){
        loggedPane.setVisible(false);
        remove(loggedPane);
        remove(enterButton);
        add(loginPane,BorderLayout.NORTH);
        loginField.setText("");
        passField.setText("");
        loginPane.add(loginField);
        loginPane.add(passField);
        loginPane.add(loginButton);
        pane.removeAll();
        pane.add(innerPane,BorderLayout.CENTER);
        setVisible(true);
    }

    private void addLoggedElements(String login){
        remove(loginPane);
        add(loggedPane,BorderLayout.NORTH);
        loggedPane.add(logoutButton,BorderLayout.EAST);
        loggingSuccessLabel.setText("You are logged in as " + login + ".");
        loggedPane.add(loggingSuccessLabel,BorderLayout.CENTER);
        loggedPane.setVisible(true);
        pane.removeAll();
        pane.add(innerPane,BorderLayout.CENTER);
        add(enterButton,BorderLayout.SOUTH);
        pane.add(textField,BorderLayout.SOUTH);
        setVisible(true);
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

    private void putMessage(String message,String time) {
        timeArea.append(time);
        textArea.append(message);
    }

    private void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLogout(){
        logoutButton.addActionListener(e->{
            System.out.println("Logging out");
            String login = loginField.getText();
            sendMessage("logout&" + login);
        });
    }

    private void handleLogin(){
        if (socket == null || socket.isClosed()) {
            initConnection();
            initReceiver();
        }
        String login = loginField.getText();
        String password = passField.getText();
        if(!login.equals("") && !password.equals("")){
            System.out.println(login + " tries to log in.");
            sendMessage("login&" + login + "&" + password);
        }
    }

    private void addLoginHandler(){
        loginButton.addActionListener(e-> handleLogin());
        loginField.addActionListener(e-> handleLogin());
        passField.addActionListener(e-> handleLogin());
    }

    private void handleClickButton(){
        enterButton.addActionListener(e -> {
            String message = textField.getText();
            if(!message.equals("")) {
                textField.setText("");
                textField.requestFocus();
                sendMessage("message&" + message);
            }
        });
    }

    private void handlePressEnter(){
        textField.addActionListener(e -> {
            String message = textField.getText();
            if(!message.equals("")) {
                textField.setText("");
                sendMessage("message&" + message);
            }
        });
    }

    private void addListeners(){
        addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                textField.requestFocusInWindow();
            }
        });
        handlePressEnter();
        handleClickButton();
        addLoginHandler();
        handleLogout();
        addCloseWindowHandler();
    }

    private void addCloseWindowHandler(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closing window");
                String login = loginField.getText();
                sendMessage("logoutFull&" + login);
            }
        });
    }
}
