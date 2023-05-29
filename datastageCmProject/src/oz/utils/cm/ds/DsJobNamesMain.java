package oz.utils.cm.ds;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.swing.joptionpane.JOptionPaneUtils;
import oz.utils.cm.ds.deployment.DsUtils;

public class DsJobNamesMain {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		getJobNames(args[0]);
	}

	private static void getJobNames(final String path) {
		logger.info("get job names for: ".concat(path));
		List<String> jobNameList = DsUtils.getJobNames(path);
		String[] jobNames = new String[jobNameList.size()];
		jobNameList.toArray(jobNames);
		String fileName = new File(path).getName();
		String title = " job names for ".concat(fileName);
		JOptionPaneUtils.showListMessageDialog(jobNames, title);
	}
}
