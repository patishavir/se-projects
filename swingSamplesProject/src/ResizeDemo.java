import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ResizeDemo {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ResizeDemo().createAndShowGUI();
			}
		});

	}

	private BufferedImage img;
	private double zoomLevel = 1;

	private void createAndShowGUI() {
		JOptionPane.showMessageDialog(null, "Please select an image.");
		JFileChooser imgChooser = new JFileChooser();
		while (img == null) {
			try {
				if (imgChooser.showDialog(null, "Open") != JFileChooser.APPROVE_OPTION) {
					System.exit(0);
				}

				img = javax.imageio.ImageIO.read(imgChooser.getSelectedFile());
				if (img.getType() == BufferedImage.TYPE_CUSTOM) {
					BufferedImage tmp = new BufferedImage(img.getWidth(), img.getHeight(),
							BufferedImage.TYPE_3BYTE_BGR);
					ColorConvertOp op = new ColorConvertOp(null);
					op.filter(img, tmp);
					img = tmp;
				}
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, "That was not an image "
						+ "format I recognized!  Please try again.");
			} catch (java.io.IOException e) {
				JOptionPane.showMessageDialog(null, "I was not able to read "
						+ "that image!  Please try again.");
			}
		}

		final JPanel resizePanel = new JPanel() {
			public Dimension getPreferredSize() {
				return new Dimension((int) (img.getWidth() * zoomLevel),
						(int) (img.getHeight() * zoomLevel));
			}

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img, 0, 0, (int) (img.getWidth() * zoomLevel),
						(int) (img.getHeight() * zoomLevel), null);
			}
		};

		JSlider slider = new JSlider(0, 1000, 100);
		slider.setBorder(BorderFactory.createTitledBorder("Zoom = 1.00"));
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				zoomLevel = ((JSlider) e.getSource()).getValue() / 100d;

				JSlider theSlider = (JSlider) e.getSource();
				((TitledBorder) theSlider.getBorder()).setTitle("Zoom = " + zoomLevel);
				theSlider.repaint();
				resizePanel.revalidate();
				resizePanel.repaint();
			}
		});

		JFrame frame = new JFrame();
		frame.add(new JScrollPane(resizePanel), java.awt.BorderLayout.CENTER);
		frame.add(slider, java.awt.BorderLayout.SOUTH);
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
