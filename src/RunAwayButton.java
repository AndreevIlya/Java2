import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

class RunAwayButton extends JFrame {
    private Random random = new Random();
    private JButton button = configButton();
    private int counter = 0;

    RunAwayButton(){
        drawWindow();
        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);
        panel.add(button);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int buttonXL = button.getX();
                int buttonXR = buttonXL + 150;
                int buttonYT = button.getY();
                int buttonYB = buttonYT + 70;
                if(counter > 10){
                    counter = 0;
                    button.setBounds(random.nextInt(835), random.nextInt(695), 150, 70);
                }else{
                    if (mouseX < 10) {
                        if (buttonXR < 850) {
                            button.setBounds(buttonXL + 150, buttonYT, 150, 70);
                        } else {
                            button.setBounds(0, buttonYT, 150, 70);
                        }
                    } else if (mouseX > 140) {
                        if (buttonXL > 150) {
                            button.setBounds(buttonXL - 150, buttonYT, 150, 70);
                        } else {
                            button.setBounds(835, buttonYT, 150, 70);
                        }
                    } else if (mouseY < 10) {
                        if (buttonYB < 730) {
                            button.setBounds(buttonXL, buttonYT + 70, 150, 70);
                        } else {
                            button.setBounds(buttonXL, 0, 150, 70);
                        }
                    } else if (mouseY > 60) {
                        if (buttonYT > 70) {
                            button.setBounds(buttonXL, buttonYT - 70, 150, 70);
                        } else {
                            button.setBounds(buttonXL, 695, 150, 70);
                        }
                    } else {
                        button.setBounds(random.nextInt(835), random.nextInt(695), 150, 70);
                    }
                    counter++;
                }
            }
        });

        setVisible(true);
    }

    private void drawWindow() {
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int)(screenSize.getWidth() - 1000) / 2;
        int y = (int)(screenSize.getHeight() - 800) / 2;
        if(x < 0) x = 0;
        if(y < 0) y = 0;
        setBounds(x,y,1000,800);
        setTitle("Убегающая кнопка");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private JButton configButton(){
        JButton button = new JButton("Catch me!");
        int x = random.nextInt(835);
        int y = random.nextInt(695);
        button.setBounds(x,y,150,70);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setBackground(new Color(0x002C06));
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setFont(new Font("Verdana",Font.ITALIC,16));
        button.setForeground(new Color(0xD2C400));

        return button;
    }
}
