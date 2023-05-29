package oz.temp.files;

import java.util.logging.Logger;

import oz.infra.io.FolderUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;

public class TempFolderUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePath1 = FolderUtils.getSingleFilePathInFolder("C:/temp/fonfon");
		logger.info("filePath1: " + filePath1);
		String filePath2 = FolderUtils.getSingleFilePathInFolder("C:/temp/fonfon", RegexpUtils.REGEXP_ZIP_FILE);
		logger.info("filePath2: " + filePath2);
	}

}