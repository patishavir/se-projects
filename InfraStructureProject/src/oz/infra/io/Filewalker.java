package oz.infra.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class Filewalker {
	private List<File> fileList = new ArrayList<File>();
	private static Logger logger = JulUtils.getLogger();

	public List<File> walk(final String rootPath) {
		File root = new File(rootPath);
		File[] list = root.listFiles();
		if (list != null) {
			for (File file1 : list) {
				if (file1.isDirectory()) {
					walk(file1.getAbsolutePath());
					logger.finest("Dir:" + file1.getAbsoluteFile());
				} else {
					logger.finest("File:" + file1.getAbsoluteFile());
					fileList.add(file1);
				}
			}
		} else {
			fileList = null;
		}
		return fileList;
	}
}