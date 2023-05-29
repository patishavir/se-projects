package oz.infra.swing.component;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Logger;

import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import oz.infra.logging.jul.JulUtils;

public class ComponentUtils {
	private static Logger logger = JulUtils.getLogger();

	public static Component findComponentUnderGlassPaneAt(final Point point,
			final Component topComponent) {
		Component component = null;

		if (topComponent.isShowing()) {
			if (topComponent instanceof RootPaneContainer) {
				component = ((RootPaneContainer) topComponent).getLayeredPane().findComponentAt(
						SwingUtilities.convertPoint(topComponent, point,
								((RootPaneContainer) topComponent).getLayeredPane()));
			} else {
				component = ((Container) topComponent).findComponentAt(point);
			}
		}

		return component;
	}

	public static Component getTopLevelAncestor(Component component) {
		while (component != null) {
			if (component instanceof Window || component instanceof Applet) {
				break;
			}
			component = component.getParent();
		}
		return component;
	}

	public static int removeAllMouseListeners(final Component component) {
		MouseListener[] mouseListeners = component.getMouseListeners();
		for (MouseListener mouseListener : mouseListeners) {
			component.removeMouseListener(mouseListener);
		}
		logger.finest(String.valueOf(mouseListeners.length)
				+ " mouseMotionListeners have been removed");
		return mouseListeners.length;
	}

	public static int removeAllMouseMotionListeners(final Component component) {
		MouseMotionListener[] mouseMotionListeners = component.getMouseMotionListeners();
		for (MouseMotionListener mouseMotionListener : mouseMotionListeners) {
			component.removeMouseMotionListener(mouseMotionListener);
		}
		logger.finest(String.valueOf(mouseMotionListeners.length)
				+ " mouseMotionListeners have been removed");
		return mouseMotionListeners.length;
	}

	public static void setAllSizes(final Component component, final Dimension dimension) {
		component.setPreferredSize(dimension);
		component.setMinimumSize(dimension);
		component.setMaximumSize(dimension);
	}
}
