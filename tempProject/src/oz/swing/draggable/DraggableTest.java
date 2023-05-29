/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package oz.swing.draggable;

/*
 * AbsoluteLayoutDemo.java requires no other files.
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.draggable.DraggableImageComponent;

public class DraggableTest {
	private static Logger logger = JulUtils.getLogger();

	public static void addComponentsToPane(Container pane) {
		pane.setLayout(null);
		pane.add(buildJPanel(0, Color.magenta));
		pane.add(buildJPanel(1, Color.PINK));
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("AbsoluteLayoutDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set up the content pane.
		addComponentsToPane(frame.getContentPane());

		// Size and display the window.
		Insets insets = frame.getInsets();
		frame.setSize(600 + insets.left + insets.right, 125 + insets.top + insets.bottom);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static JPanel buildJPanel(final int i, final Color bgColor) {
		JPanel jPanel = new JPanel();
		DraggableImageComponent b1 = new DraggableImageComponent();
		b1.addMouseListener(new DraggableMouseAdapter(b1));
		b1.addMouseMotionListener(new DraggableMouseAdapter(b1));
		DraggableImageComponent b2 = new DraggableImageComponent();
		b2.addMouseListener(new DraggableMouseAdapter(b2));
		b2.addMouseMotionListener(new DraggableMouseAdapter(b2));
		DraggableImageComponent b3 = new DraggableImageComponent();
		b3.addMouseListener(new DraggableMouseAdapter(b3));
		b3.addMouseMotionListener(new DraggableMouseAdapter(b3));

		b1.setBackground(Color.BLUE);
		b2.setBackground(Color.YELLOW);
		b3.setBackground(Color.RED);

		// jPanel = (JPanel) pane;
		jPanel.setLayout(null);
		jPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
		jPanel.setBackground(bgColor);
		jPanel.setOpaque(true);

		jPanel.add(b1);
		jPanel.add(b2);
		jPanel.add(b3);

		jPanel.setPreferredSize(new Dimension(300, 125));
		jPanel.setBounds(5 + i * 300, 5, 300, 125);
		Insets insets = jPanel.getInsets();
		Dimension size = b1.getPreferredSize();
		b1.setBounds(25 + insets.left, 5 + insets.top, size.width + 20, size.height + 20);
		size = b2.getPreferredSize();
		b2.setBounds(55 + insets.left, 40 + insets.top, size.width + 40, size.height + 40);
		size = b3.getPreferredSize();
		b3.setBounds(150 + insets.left, 15 + insets.top, size.width + 50, size.height + 20);
		return jPanel;
	}
}
