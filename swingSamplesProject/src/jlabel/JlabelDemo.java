package jlabel;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JlabelDemo extends JPanel {
    JLabel jlbLabel1, jlbLabel2, jlbLabel3;

    public JlabelDemo() {
    	
        ImageIcon icon = new ImageIcon("java-swing-tutorial.JPG", "My Website");		//Creating an Icon 
        
        setLayout(new GridLayout(3,1));	//3 rows, 1 column Panel having Grid Layout

        jlbLabel1 = new JLabel("Image with Text", icon, JLabel.CENTER);
        
        //We can position of the text, relative to the icon:
        jlbLabel1.setVerticalTextPosition(JLabel.BOTTOM);
        jlbLabel1.setHorizontalTextPosition(JLabel.CENTER);

        jlbLabel2 = new JLabel("Text Only Label");

        jlbLabel3 = new JLabel(icon);		//Label of Icon Only

        //Add labels to the Panel
        add(jlbLabel1);
        add(jlbLabel2);
        add(jlbLabel3);
    }

    public static void main(String[] args) {
     
        JFrame frame = new JFrame("jLabel Usage Demo");

        frame.addWindowListener(new WindowAdapter() {	//Shows code to Add Window Listener
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        frame.setContentPane(new JlabelDemo());
        frame.pack();
        frame.setVisible(true);
    }
}