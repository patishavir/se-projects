package graphics;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AlphaCompositeCLEAR extends JFrame {
  public AlphaCompositeCLEAR() {
    getContentPane().add(new DrawingCanvas());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(300,300);
    setVisible(true);
  }

  public static void main(String arg[]) {
    new AlphaCompositeCLEAR();
  }
}

class DrawingCanvas extends JPanel{
  float alphaValue = 1.0f;
  int compositeRule = AlphaComposite.CLEAR;
  AlphaComposite ac;

  DrawingCanvas() {
    setSize(300, 300);
    setBackground(Color.white);
  }

  public void paint(Graphics g) {
    Graphics2D g2D = (Graphics2D) g;

    int w = getSize().width;
    int h = getSize().height;

    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D big = bi.createGraphics();

    ac = AlphaComposite.getInstance(compositeRule, alphaValue);

    big.setColor(Color.red);
    big.drawString("Destination", w / 4, h / 4);
    big.fill(new Ellipse2D.Double(0, h / 3, 2 * w / 3, h / 3));

    big.setColor(Color.blue);
    big.drawString("Source", 3 * w / 4, h / 4);

    big.setComposite(ac);
    big.fill(new Ellipse2D.Double(w / 3, h / 3, 2 * w / 3, h / 3));

    g2D.drawImage(bi, null, 0, 0);
  }
}