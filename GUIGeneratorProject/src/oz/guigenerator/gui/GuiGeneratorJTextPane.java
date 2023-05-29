package oz.guigenerator.gui;

import javax.swing.JTextPane;

public class GuiGeneratorJTextPane extends JTextPane {
	private String url;

	public final String getUrl() {
		return url;
	}

	public final void setUrl(final String url) {
		this.url = url;
	}
}
