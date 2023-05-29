package oz.monitor.monitors;

import java.io.File;
import java.util.Properties;

import oz.infra.io.FileUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.string.StringUtils;
import oz.monitor.common.OzMonitorResponse;

public class FileContentsMonitor extends AbstractOzMonitor {
	private String filePath;
	private String string2Look4;
	private String stringExistsOK = "no";
	private boolean booleanStringExistsOK = false;
	private File file2Monitor = null;

	public FileContentsMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
		file2Monitor = new File(filePath);
		booleanStringExistsOK = stringExistsOK.equalsIgnoreCase("yes");
		logger.info(StringUtils.concat("Starting monitor ", this.getClass().getName(), " resource: ", getResource(),
				" filePath: ", filePath, " string to look for: ", string2Look4, " stringExistsOK: ", stringExistsOK));
	}

	public final OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		boolean isResourceAvailable = false;
		String ozMonitorMessage = filePath + " does not exist !";
		logger.finest(StringUtils.concat("Monitoring resource: ", getResource(), ". file path: ", filePath,
				" looking for string ", string2Look4));
		int index = -1;
		boolean file2MonitorExists = file2Monitor.exists();
		if (file2MonitorExists) {
			String fileContents = FileUtils.readTextFile(file2Monitor);
			index = fileContents.indexOf(string2Look4);
			isResourceAvailable = ((index > -1) && booleanStringExistsOK) || ((index < 0) && (!booleanStringExistsOK));
			if (isResourceAvailable) {
				ozMonitorMessage = StringUtils.concat("String ", string2Look4, " has not been found in ", filePath);
			} else {
				ozMonitorMessage = StringUtils.concat("String ", string2Look4, " has been found in ", filePath, " !");
			}
		}
		OzMonitorResponse ozMonitorResponse = generateOzMonitorResponse(isResourceAvailable, ozMonitorMessage, null);
		ozMonitorResponse.setPerformOnFailureAction(file2MonitorExists);
		return ozMonitorResponse;
	}

	public final void setFilePath(final String filePath) {
		this.filePath = filePath;
	}

	public final void setString2Look4(final String string2Look4) {
		this.string2Look4 = string2Look4;
	}

	public final void setStringExistsOK(final String stringExistsOK) {
		this.stringExistsOK = stringExistsOK;
	}
}
