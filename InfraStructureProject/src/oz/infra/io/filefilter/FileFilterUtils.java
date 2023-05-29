package oz.infra.io.filefilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class FileFilterUtils {
	private static Logger logger = JulUtils.getLogger();

	public static List<File> filerFileListByRegExp(final List<File> fileList, final String regexp) {
		FileFilterRegExpression fileFilterRegExpression = new FileFilterRegExpression(regexp);
		List<File> filteredFileList1 = new ArrayList<File>();
		for (File file1 : fileList) {
			if (fileFilterRegExpression.accept(file1)) {
				filteredFileList1.add(file1);
				logger.finest("accepted: " + file1.getName());
			}
		}
		logger.finest("size: " + String.valueOf(filteredFileList1.size()));
		return filteredFileList1;
	}

}
