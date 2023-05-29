package oz.swing.draggable;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.draggable.DraggableComponent;
import oz.infra.swing.point.PointUtils;

public class DraggableMouseAdapter extends MouseAdapter {
	private static Logger logger = JulUtils.getLogger();

	private DraggableComponent handle = null;
	protected Point anchorPoint;

	public DraggableMouseAdapter(final DraggableComponent draggableComponent) {
		this.handle = draggableComponent;
		logger.fine("MyMouseAdapter construction has completed");
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		anchorPoint = me.getPoint();
		handle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		logger.fine("mouse event " + me.toString() + " has been processed. Anchor: "
				+ PointUtils.asString(anchorPoint));
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		int anchorX = anchorPoint.x;
		int anchorY = anchorPoint.y;

		Point parentOnScreen = handle.getParent().getLocationOnScreen();
		Point mouseOnScreen = me.getLocationOnScreen();
		Point position = new Point(mouseOnScreen.x - parentOnScreen.x - anchorX, mouseOnScreen.y
				- parentOnScreen.y - anchorY);
		handle.setLocation(position);
		logger.info(PointUtils.asString(anchorPoint, "anchorPoint"));
		logger.info(PointUtils.asString(parentOnScreen, "parentOnScreen"));
		logger.info(PointUtils.asString(mouseOnScreen, "mouseOnScreen"));
		logger.info(PointUtils.asString(position, "position"));
		// Change Z-Buffer if it is "overbearing"
		if (handle.isOverbearing()) {
			handle.getParent().setComponentZOrder(handle, 0);
			handle.repaint();
		}
		logger.info("mouse event " + me.toString() + " has been processed. Position: "
				+ PointUtils.asString(position));
	}

	public void mouseReleased(MouseEvent me) {
		logger.fine(me.toString());
	}
}
