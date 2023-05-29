package oz.swing;

import java.awt.Container;

import javax.swing.JFrame;

import oz.infra.swing.container.ContainerUtils;

public class ImagePanelMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame jFrame = new JFrame();
		Container container = jFrame.getContentPane();
		container.add(new ImagePanel());
		jFrame.pack();
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ContainerUtils.refreshDisplay(jFrame);
		jFrame.setVisible(true);

	}

}
