package oz.infra.swing.draggable;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.point.PointUtils;

public class DraggableMouseAdapter extends MouseAdapter {
	private static Logger logger = JulUtils.getLogger();

	protected DraggableComponent draggableComponent = null;
	protected Point anchorPoint;

	public DraggableMouseAdapter(final DraggableComponent draggableComponent) {
		this.draggableComponent = draggableComponent;
		logger.finer(this.getClass().toString() + " construction has completed");
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		anchorPoint = me.getPoint();
		// draggableComponent.setCursor(draggableComponent.getDraggingCursor());
		logger.finest("mouse event " + me.toString() + " has been processed. Anchor: "
				+ PointUtils.asString(anchorPoint));
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		int anchorX = anchorPoint.x;
		int anchorY = anchorPoint.y;

		Point parentOnScreen = draggableComponent.getParent().getLocationOnScreen();
		Point mouseOnScreen = me.getLocationOnScreen();
		Point position = new Point(mouseOnScreen.x - parentOnScreen.x - anchorX, mouseOnScreen.y
				- parentOnScreen.y - anchorY);
		draggableComponent.setLocation(position);
		StringBuilder sb = new StringBuilder("\n");
		sb.append(PointUtils.asString(anchorPoint, "anchorPoint"));
		sb.append("\n");
		sb.append(PointUtils.asString(parentOnScreen, "parentOnScreen"));
		sb.append("\n");
		sb.append(PointUtils.asString(mouseOnScreen, "mouseOnScreen"));
		sb.append("\n");
		sb.append(PointUtils.asString(position, "position"));
		logger.finest(sb.toString());
		// Change Z-Buffer if it is "overbearing"
		if (draggableComponent.isOverbearing()) {
			draggableComponent.getParent().setComponentZOrder(draggableComponent, 0);
			draggableComponent.repaint();
		}
		logger.finer("mouse event " + me.toString() + " has been processed. Position: "
				+ PointUtils.asString(position));
	}

	public void mouseReleased(MouseEvent me) {
		logger.finest(me.toString());
	}
}
