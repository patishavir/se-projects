import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

public class GetXDemo extends JFrame {
  JComponent comp = new JLabel("Test label");

  public GetXDemo() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    comp.setToolTipText("Some tooltip text for component");
    comp.setBorder(new TitledBorder("Button"));

    System.out.println("X:" + comp.getX() + " Y:" + comp.getY() + " width:"
        + comp.getWidth() + " height:" + comp.getHeight());

    getContentPane().add(comp);
    pack();

    System.out.println("X:" + comp.getX() + " Y:" + comp.getY() + " width:"
        + comp.getWidth() + " height:" + comp.getHeight());

    setVisible(true);
  }

  public static void main(String arg[]) {
    new GetXDemo();
  }
}

