import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Application extends JFrame {
    private JButton buttonEnter = configButton();
    private JTextField textField = configTextField();
    private JTextArea textArea = configTextArea();
    private JPanel pane = configPane();


    Application(){
        drawWindow();
        setElements();
        addListeners();

        setVisible(true);
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
        pane.add(new JScrollPane(textArea),BorderLayout.CENTER);
    }

    private JPanel configPane(){
        JPanel pane = new JPanel();
        pane.setBackground(new Color(0xcccccc));
        pane.setLayout(new BorderLayout());

        return pane;
    }

    private JButton configButton(){
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

    private JTextField configTextField(){
        JTextField textField = new JTextField();
        textField.setHorizontalAlignment(SwingConstants.LEFT);
        textField.setMargin(new Insets(20,50,20,50));
        textField.setBackground(new Color(0xeeeeee));
        textField.setOpaque(true);
        textField.setFont(new Font("Verdana",Font.ITALIC,18));
        textField.setForeground(new Color(0x444444));

        return textField;
    }

    private JTextArea configTextArea(){
        JTextArea textArea = new JTextArea();
        textArea.setMargin(new Insets(20,50,20,50));
        textArea.setFont(new Font("Verdana",Font.PLAIN,18));

        return textArea;
    }

    private void putText(){
        String text = textField.getText();
        if(!text.equals("")){
            textField.setText("");
            textArea.append(text + "\n");
        }
    }
    private void clickButton(){
        buttonEnter.addActionListener(e -> putText());
    }

    private void pressEnter(){
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    putText();
                }
            }
        });
    }

    private void addListeners(){
        addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                textField.requestFocusInWindow();
            }
        });
        pressEnter();
        clickButton();
    }
}
