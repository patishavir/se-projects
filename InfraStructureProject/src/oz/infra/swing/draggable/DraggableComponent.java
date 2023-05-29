package oz.infra.swing.draggable;

/*
 *  Copyright 2010 De Gregorio Daniele.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.util.logging.Logger;

import javax.swing.JComponent;

import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.component.ComponentUtils;

public class DraggableComponent extends JComponent {
	private static Logger logger = JulUtils.getLogger();
	/** If sets <b>TRUE</b> this component is draggable */
	private boolean draggable = false;
	/**
	 * 2D Point representing the coordinate where mouse is, relative parent
	 * container
	 */

	/** Default mouse cursor for dragging action */
	protected Cursor draggingCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

	/**
	 * If sets <b>TRUE</b> when dragging component, it will be painted over each
	 * other (z-Buffer change)
	 */
	protected boolean overbearing = false;

	public DraggableComponent() {
		setOpaque(true);
	}

	/**
	 * We have to define this method because a JComponent is a void box. So we
	 * have to define how it will be painted. We create a simple filled
	 * rectangle.
	 * 
	 * @param g
	 *            Graphics object as canvas
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	/**
	 * Set the value of draggable
	 * 
	 * @param draggable
	 *            new value of draggable
	 */
	public void setDraggable(final boolean draggable, final MouseAdapter mouseAdapter) {
		this.draggable = draggable;
		if (draggable) {
			addMouseListener(mouseAdapter);
			addMouseMotionListener(mouseAdapter);
			setCursor(draggingCursor);
			logger.finest("Draggable has been set to true. Cursor  has been set to draggingCursor.");
		} else {
			setCursor(Cursor.getDefaultCursor());
			int ml = ComponentUtils.removeAllMouseListeners(this);
			int mml = ComponentUtils.removeAllMouseMotionListeners(this);
			logger.finest("Draggable has been set to false. Cursor  has been set to default. "
					+ String.valueOf(ml + mml) + " listeners have been removed.");
		}
	}

	/**
	 * Get the value of draggingCursor
	 * 
	 * @return the value of draggingCursor
	 */
	public Cursor getDraggingCursor() {
		return draggingCursor;
	}

	/**
	 * Set the value of draggingCursor
	 * 
	 * @param draggingCursor
	 *            new value of draggingCursor
	 */
	public void setDraggingCursor(Cursor draggingCursor) {
		this.draggingCursor = draggingCursor;
	}

	/**
	 * Get the value of overbearing
	 * 
	 * @return the value of overbearing
	 */
	public boolean isOverbearing() {
		return overbearing;
	}

	/**
	 * Set the value of overbearing
	 * 
	 * @param overbearing
	 *            new value of overbearing
	 */
	public void setOverbearing(boolean overbearing) {
		this.overbearing = overbearing;
	}
}
