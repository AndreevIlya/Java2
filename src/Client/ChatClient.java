package Client;

import History.History;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

class ChatClient extends JFrame{
    final JButton enterButton = createSendButton("Enter");
    private final JButton loginButton = createSendButton("Log in");
    private final JButton logoutButton = createSendButton("Log out");
    final JTextField textField = createTextField();
    private final JTextField loginField = createTextField();
    private final JTextField passField = createTextField();
    final JTextArea textArea = createTextArea();
    private final JTextArea timeArea = createTextArea();
    private final JPanel pane = createPane();
    private final JPanel innerPane = createColumnPane(2);
    private final JPanel loginPane = createColumnPane(3);
    private final JPanel loggedPane = createPane();
    private final JLabel loggingFailureLabel = createLabel();
    private final JLabel loggingSuccessLabel = createLabel();
    private final JScrollPane scrollTextArea = new JScrollPane(textArea);
    private final JScrollPane scrollTimeArea = new JScrollPane(timeArea);

    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private boolean logged = false;
    private boolean showHistoryAtLogin = true;

    private final Map<String,Responder> responderMap = initResponderMap();
    private static History clientHistory;


    ChatClient(){
        drawWindow();
        setElementsInit();
        addListeners();
        try {
            initConnection();
        } catch (ConnectException e) {
            reConnect();
        }
        initReceiver();
        setVisible(true);
    }

    private void initConnection() throws ConnectException{
        try {
            socket = new Socket("192.168.1.103", 4444);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            System.out.println("Connection initialized");
        } catch (ConnectException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initReceiver(){
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    handleInput();
                }catch (SocketException exc){
                    textArea.append("Server is down.\n");
                    try {
                        socket.close();
                        reConnect();
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        System.out.println("receiver started");
    }

    private void handleInput() throws SocketException{
        if (!socket.isClosed()) {
            try {
                String[] data = inputStream.readUTF().split("&");
                System.out.println(data[0]);
                responderMap.get(data[0]).respond(data);
            } catch (SocketException exc){
                throw exc;
            }catch(EOFException exc){
                System.out.println("Full halt.");
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, Responder> initResponderMap() {
        Map<String, Responder> map = new HashMap<>();
        map.put("fail", s -> {
            System.out.println("failed to login");
            ChatClient.this.addLogFailureLabel("Login is already occupied and has another password.");
        });
        map.put("occupied", s -> {
            System.out.println(loginField.getText() + " is already logged in.");
            addLogFailureLabel(loginField.getText() + " is already logged in.");
        });
        map.put("logged", s -> {
            addLoggedElements(loginField.getText());
            if(showHistoryAtLogin){
                clientHistory = new History("History", "history_" + loginField.getText(), "history.txt");
                String[] oldHistory = clientHistory.splitHistory();
                textArea.append(oldHistory[0]);
                timeArea.append(oldHistory[1]);
                showHistoryAtLogin = false;
            }
            System.out.println("Logged in.");
            textField.requestFocus();
            logged = true;
        });
        map.put("logout", s -> {
            addLoginPane();
            System.out.println("Logged out.");
            logged = false;
        });
        map.put("message", s -> {
            System.out.println("Received " + s[1] + " at " + s[2]);
            textArea.append(s[1]);
            timeArea.append(s[2]);
            clientHistory.writeHistory(s[1] + "&" + s[2] + "&");
        });
        return map;
    }

    private interface Responder {
        void respond(String[] s);
    }

    void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reConnect(){
        int attempt;
        for(attempt = 0; attempt < 5;attempt++){
            try {
                textArea.append("Trying to connect...\n");
                Thread.sleep(10000);
                initConnection();
                initReceiver();
                textArea.append("Connection reestablished.\n");
                sendMessage("reconnect");
                if(logged) handleLogin();
                break;
            } catch (ConnectException e) {
                textArea.append("Reconnection failed.\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(attempt == 5) textArea.append("Unable to connect. Try again later.\n");
    }

    private void addListeners(){
        addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                textField.requestFocusInWindow();
            }
        });
        addPressEnterHandler();
        addClickButtonHandler();
        addLoginHandler();
        addLogoutHandler();
        addCloseWindowHandler();
        addScrollHandler();
    }

    private void addLogoutHandler(){
        logoutButton.addActionListener(e->{
            System.out.println("Logging out");
            String login = loginField.getText();
            sendMessage("logout&" + login);
        });
    }

    private void handleLogin(){
        String login = loginField.getText();
        String password = passField.getText();
        if(!login.trim().isEmpty() && !password.isEmpty()){
            System.out.println(login + " tries to log in.");
            sendMessage("login&" + login + "&" + password);
        }
    }

    private void addLoginHandler(){
        loginButton.addActionListener(e-> handleLogin());
        loginField.addActionListener(e-> handleLogin());
        passField.addActionListener(e-> handleLogin());
    }

    void addClickButtonHandler(){
        enterButton.addActionListener(e -> {
            String message = textField.getText();
            if(!message.equals("")) {
                textField.setText("");
                textField.requestFocus();
                System.out.println(message);
                if(message.charAt(0) == 'p' && message.charAt(1) == 'm' && message.charAt(2) == '&'){
                    sendMessage(message);
                }else{
                    sendMessage("message&" + message);
                }
            }
        });
    }

    void addPressEnterHandler(){
        textField.addActionListener(e -> {
            String message = textField.getText();
            if(!message.equals("")) {
                textField.setText("");
                System.out.println(message);
                if(message.charAt(0) == 'p' && message.charAt(1) == 'm' && message.charAt(2) == '&'){
                    sendMessage(message);
                }else{
                    sendMessage("message&" + message);
                }
            }
        });
    }

    private void addCloseWindowHandler(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closing window");
                if(!socket.isClosed()){
                    if (logged){
                        String login = loginField.getText();
                        sendMessage("logoutFull&" + login);
                    }else{
                        sendMessage("exit");
                    }
                }
            }
        });
    }

    private void addScrollHandler(){
        scrollTextArea.getVerticalScrollBar().getAccessibleContext()
                .addPropertyChangeListener((PropertyChangeEvent e) -> {
                    int scrollValue = scrollTextArea.getVerticalScrollBar().getValue();
                    scrollTimeArea.getVerticalScrollBar().setValue(scrollValue);
                });
        scrollTimeArea.getVerticalScrollBar().getAccessibleContext()
                .addPropertyChangeListener((PropertyChangeEvent e) -> {
                    int scrollValue = scrollTimeArea.getVerticalScrollBar().getValue();
                    scrollTextArea.getVerticalScrollBar().setValue(scrollValue);
                });
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
        textArea.setText("Start message with \'pm&\' \nto write a private message. \nLike pm&Ilya&.\n");
        timeArea.setText("\n\n\n");
        innerPane.add(scrollTextArea);
        innerPane.add(scrollTimeArea);
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
}
