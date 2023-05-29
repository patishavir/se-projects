package jlabel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * A template for animation applications.
 */
public class Animator extends JFrame implements ActionListener {
	int frameNumber = -1;
	Timer timer;
	boolean frozen = false;
	JLabel label;
	private FlowLayout xYLayout1 = new FlowLayout();
	private FlowLayout xYLayout2 = new FlowLayout();
	private JPanel jPanel1 = new JPanel();

	Animator(int fps, String windowTitle) {
		super(windowTitle);
		int delay = (fps > 0) ? (1000 / fps) : 100;

		// Set up a timer that calls this object's action handler.
		timer = new Timer(delay, this);
		timer.setInitialDelay(0);
		timer.setCoalesce(true);

		addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {
				stopAnimation();
			}

			public void windowDeiconified(WindowEvent e) {
				startAnimation();
			}

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		getContentPane().setSize(800, 800);
		getContentPane().add(jPanel1);
		label = new JLabel("Frame     ");
		label.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (frozen) {
					frozen = false;
					startAnimation();
				} else {
					frozen = true;
					stopAnimation();
				}
			}
		});

		jPanel1.add(label);
	}

	// Can be invoked by any thread (since timer is thread-safe).
	public void startAnimation() {
		if (frozen) {
			// Do nothing. The user has requested that we
			// stop changing the image.
		} else {
			// Start animating!
			if (!timer.isRunning()) {
				timer.start();
			}
		}
	}

	// Can be invoked by any thread (since timer is thread-safe).
	public void stopAnimation() {
		// Stop the animating thread.
		if (timer.isRunning()) {
			timer.stop();
		}
	}

	public void actionPerformed(ActionEvent e) {
		// Advance the animation frame.
		frameNumber++;
		label.setText("Frame " + frameNumber);
		label.setLocation(frameNumber, frameNumber);
	}

	public static void main(String args[]) {
		Animator animator = null;
		int fps = 10;

		// Get frames per second from the command line argument.
		if (args.length > 0) {
			try {
				fps = Integer.parseInt(args[0]);
			} catch (Exception e) {
			}
		}
		animator = new Animator(fps, "Animator with Timer");
		animator.pack();
		animator.setVisible(true);

		// It's OK to start the animation here because
		// startAnimation can be invoked by any thread.
		animator.startAnimation();
	}

	public Animator() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(xYLayout1);
		jPanel1.setLayout(xYLayout2);
		this.getContentPane().add(jPanel1);
	}
}