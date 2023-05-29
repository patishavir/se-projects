package image;

import java.awt.Dimension;

import javax.swing.JFrame;

public class ImageDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ImagePanel imagePanel = new ImagePanel(
				"C:\\oj\\projects\\hujiProject\\images\\Balfur 29.jpg");
		JFrame jFrame = new javax.swing.JFrame();
		jFrame.add(imagePanel);
		Dimension preferredSize = new Dimension(300, 300);
		jFrame.setPreferredSize(preferredSize);
		jFrame.pack();
		jFrame.setVisible(true);
	}
}
