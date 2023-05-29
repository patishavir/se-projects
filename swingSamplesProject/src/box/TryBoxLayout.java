package box;
import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
public class TryBoxLayout {
  public static void main(String[] args) {
    JFrame aWindow = new JFrame("This is a Box Layout");
    aWindow.setBounds(30, 30, 300, 300);
    aWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Create left column of radio buttons
    Box left = Box.createVerticalBox();
    ButtonGroup radioGroup = new ButtonGroup();
    JRadioButton rbutton;
    radioGroup.add(rbutton = new JRadioButton("Red"));
    left.add(rbutton);
    radioGroup.add(rbutton = new JRadioButton("Green"));
    left.add(rbutton);
    radioGroup.add(rbutton = new JRadioButton("Blue"));
    left.add(rbutton);
    radioGroup.add(rbutton = new JRadioButton("Yellow"));
    left.add(rbutton);
    Box right = Box.createVerticalBox();
    right.add(new JCheckBox("A"));
    right.add(new JCheckBox("B"));
    right.add(new JCheckBox("C"));
    Box top = Box.createHorizontalBox();
    top.add(left);
    top.add(right);
    Container content = aWindow.getContentPane();
    content.setLayout(new BorderLayout());
    content.add(top, BorderLayout.CENTER);
    aWindow.pack();
    aWindow.setVisible(true);
  }
}