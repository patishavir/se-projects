package oz.infra.logging.log4j;

import java.util.Properties;
import java.util.logging.Logger;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.mq.MQUtils;
import oz.infra.properties.PropertiesUtils;

public class MqAppender extends AppenderSkeleton {
	private Logger logger = JulUtils.getLogger();
	private String writeMqMessagePropertiesPath = null;
	private Properties writeMqMessageProperties = null;

	public void activateOptions() {
		logger.fine("Staring activateOptions");
		writeMqMessageProperties = PropertiesUtils.loadPropertiesFile(writeMqMessagePropertiesPath);
	}

	public final synchronized void append(final LoggingEvent event) {
		logger.fine("Staring append");
		writeMqMessageProperties.put("message", layout.format(event));
		MQUtils.putMessage(writeMqMessageProperties);
	}

	public final synchronized void close() {
		logger.fine("Staring close");
	}

	public final boolean requiresLayout() {
		logger.fine("Staring requiresLayout");
		return true;
	}

	public final String getWriteMqMessagePropertiesPath() {
		return writeMqMessagePropertiesPath;
	}

	public final void setWriteMqMessagePropertiesPath(final String writeMqMessagePropertiesPath) {
		this.writeMqMessagePropertiesPath = writeMqMessagePropertiesPath;
		String absoluteWriteMqMessagePropertiesPath = PathUtils.getAbsolutePath(writeMqMessagePropertiesPath);
		FileUtils.terminateIfFileDoesNotExist(absoluteWriteMqMessagePropertiesPath);
	}
}
