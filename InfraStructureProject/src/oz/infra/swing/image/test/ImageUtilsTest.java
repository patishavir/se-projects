package oz.infra.swing.image.test;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.image.ImageUtils;

public class ImageUtilsTest {
	private BufferedImage img;
	private double zoomLevel = 1;
	private String imageFilePath = "C:\\JFST\\args\\images\\changing\\00_lili_glas_right.jpg";
	private int newWidth = 150;
	private int newHeight = 150;
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ImageUtilsTest().createAndShowGUI();
			}
		});

	}

	private void createAndShowGUI() {
		BufferedImage bufferedImage = null;
		while (img == null) {
			img = ImageUtils.getResizedBufferedImage(imageFilePath, newWidth, newHeight);
			// File imageFile = new File(imageFilePath);
			// try {
			// bufferedImage = javax.imageio.ImageIO.read(imageFile);
			// } catch (Exception ex) {
			// logger.info(ex.getMessage());
			// ex.printStackTrace();
			// }
			// img = ImageUtils
			// .getResizedBufferedImage(bufferedImage, newWidth, newHeight);
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
