package oz.infra.swing.container;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.dimension.DimensionUtils;

public final class ContainerUtils {
	private static Logger logger = JulUtils.getLogger();

	private ContainerUtils() {
		super();
	}

	public static void refreshDisplay(final Container container) {
		// container.invalidate();
		container.validate();
		container.repaint();
	}

	public static boolean isComponentInContainer(final Container container,
			final Component component) {
		Component[] componentsInContainer = container.getComponents();
		for (Component component1InContainer : componentsInContainer) {
			logger.finest("component1InContainer " + component1InContainer.toString());
			if (component1InContainer == component) {
				return true;
			}
		}
		return false;
	}

	public static void listComponentsInContainer(final Container container) {
		Component[] componentsInContainer = container.getComponents();
		for (Component component1InContainer : componentsInContainer) {
			logger.finest(component1InContainer.toString());
			if (component1InContainer instanceof Container) {
				ContainerUtils.listComponentsInContainer((Container) component1InContainer);
			}
		}
	}

	public static void positionComponentCentrallyInParent(final Component component) {
		Container container = component.getParent();
		Dimension parentSize = null;
		int x = 0;
		int y = 0;

		if (container == null) {
			parentSize = Toolkit.getDefaultToolkit().getScreenSize();
		} else {
			parentSize = container.getSize();
			Point xy = container.getLocation();
			x = xy.x;
			y = xy.y;
		}
		DimensionUtils.getAsString(parentSize, Level.FINEST);
		Dimension componentSize = component.getSize();
		int parentWidth = parentSize.width;
		int parentHiegth = parentSize.height;
		int componentWidth = componentSize.width;
		int componentHiegth = componentSize.height;
		x = x + (parentWidth - componentWidth) / 2;
		y = y + (parentHiegth - componentHiegth) / 2;
		logger.finest("x: " + String.valueOf(x) + " y: " + String.valueOf(y));
		component.setLocation(x, y);
		DimensionUtils.getAsString(componentSize, Level.FINEST);
	}

	public static List<Component> getAllChildContainers(final Container container) {
		Component[] components = container.getComponents();
		List<Component> containersList = new ArrayList<Component>();
		for (Component component : components) {
			if (component instanceof Container) {
				containersList.add((Container) component);
				containersList.addAll(getAllChildContainers((Container) component));
				logger.finest(component.toString());
			}
		}
		return containersList;
	}
}
