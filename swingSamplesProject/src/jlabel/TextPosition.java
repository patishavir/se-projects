package jlabel;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalIconFactory;

public class TextPosition {

  public static void main(String args[]) {
    JFrame frame = new JFrame("Label Text Pos");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.setLayout(new GridLayout(2, 2));

    Border border = LineBorder.createGrayLineBorder();
    Icon warnIcon = MetalIconFactory.getTreeComputerIcon();

    JLabel label1 = new JLabel(warnIcon);
    label1.setText("Left-Bottom");
    label1.setHorizontalTextPosition(JLabel.LEFT);
    label1.setVerticalTextPosition(JLabel.BOTTOM);
    label1.setBorder(border);
    frame.add(label1);

    JLabel label2 = new JLabel(warnIcon);
    label2.setText("Right-TOP");
    label2.setHorizontalTextPosition(JLabel.RIGHT);
    label2.setVerticalTextPosition(JLabel.TOP);
    label2.setBorder(border);
    frame.add(label2);

    JLabel label3 = new JLabel(warnIcon);
    label3.setText("Center-Center");
    label3.setHorizontalTextPosition(JLabel.CENTER);
    label3.setVerticalTextPosition(JLabel.CENTER);
    label3.setBorder(border);
    frame.add(label3);

    JLabel label4 = new JLabel(warnIcon);
    label4.setText("Center-Bottom");
    label4.setHorizontalTextPosition(JLabel.CENTER);
    label4.setVerticalTextPosition(JLabel.BOTTOM);
    label4.setBorder(border);
    frame.add(label4);

    frame.setSize(300, 200);
    frame.setVisible(true);
  }
}