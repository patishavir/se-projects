package jlabel;

import java.awt.FlowLayout;
import java.awt.HeadlessException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class JLabelWithIcon extends JFrame {
    public JLabelWithIcon() throws HeadlessException {
        initialize();
    }

    private void initialize() {
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.LEFT));

        Icon icon = new ImageIcon("ledgreen.png");
        JLabel label1 = new JLabel("Full Name :", icon, JLabel.LEFT);

        JLabel label2 = new JLabel("Address :", JLabel.LEFT);
        label2.setIcon(new ImageIcon("ledyellow.png"));

        getContentPane().add(label1);
        getContentPane().add(label2);        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JLabelWithIcon().setVisible(true);
            }
        });
    }
}