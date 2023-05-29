package oz.jdir.gui.listeners;

import javax.swing.BoundedRangeModel;
import javax.swing.JScrollBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class is used for synchronizing two JScrollBars.
 */
public class ScrollBarSynchronizer implements ChangeListener {
	private JScrollBar destinationScrollBar;

	public ScrollBarSynchronizer(final JScrollBar destinationScrollBarP) {
		destinationScrollBar = destinationScrollBarP;
	}

	public final void stateChanged(final ChangeEvent e) {
		if (!destinationScrollBar.hasFocus()) {
			BoundedRangeModel sourceScroll = (BoundedRangeModel) e.getSource();
			destinationScrollBar.setValue(sourceScroll.getValue());
		}
	}
}