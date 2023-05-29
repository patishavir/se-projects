package joptionpane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MyApp extends JFrame {
    public static void main(String [] args) {
        new MyApp();
    }

    public MyApp() {
        super("MyApp");
        setUndecorated(true);
        setVisible(true);
        setLocationRelativeTo(null);
        String i = JOptionPane.showInputDialog(this, "Enter your name:", getTitle(), JOptionPane.QUESTION_MESSAGE);
        if(null != i) {
            JOptionPane.showInputDialog(this, "Your name is:", getTitle(), JOptionPane.INFORMATION_MESSAGE, null, null, i.concat(i));
        }
        dispose();
    }
}