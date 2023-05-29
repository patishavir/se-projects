package oz.infra.swing.jbutton.test;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import oz.infra.swing.jbutton.CircularButton;
import oz.infra.swing.jbutton.OvalJButtom;

public class TestJButton {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame jf = new JFrame();
		Container contentPane = jf.getContentPane();
		contentPane.setLayout(new FlowLayout());
		OvalJButtom ovalJButtom = new OvalJButtom("oval jbutton");
		jf.add(ovalJButtom);
		CircularButton circularButton = new CircularButton("circular jbutton");
		jf.add(circularButton);
		ovalJButtom.setBorder(BorderFactory.createLineBorder(Color.RED));
		jf.pack();
		jf.setVisible(true);
	}
}
