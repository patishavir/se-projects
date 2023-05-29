package oz.infra.io.tailer;

import java.util.logging.Logger;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

import oz.infra.logging.jul.JulUtils;

public class TailerListenerImpl extends TailerListenerAdapter {

	private String filePath = null;
	private static Logger logger = JulUtils.getLogger();

	public void handle(final String line) {
		logger.info(line);

	}

	public void init(final Tailer tailer) {
		logger.info(tailer.toString());
		filePath = tailer.getFile().getAbsolutePath();

	}

	public void fileNotFound() {
		logger.info(filePath.concat(" not found ..."));

	}

	public void handle(Exception ex) {
		logger.info(ex.toString());

	}

	public void fileRotated() {
		logger.info(filePath.concat(" rotated ..."));

	}

}
