package oz.temp.files;

import java.util.logging.Logger;

import oz.infra.io.FolderUtils;
import oz.infra.logging.jul.JulUtils;

public class Folder111 {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// FolderUtils.getFileListWithInNameRange("C:/temp/New folder",
		// "dbchanges_52806-dbchanges_52821");
		// logger.info(PropertiesUtils.getAsDelimitedString(SystemUtils.getSystemProperties()));
		// FolderUtils.createTempFolder();
		FolderUtils.copySelectedFiles2Folder("C:/temp/New folder", "dbchanges_52806-dbchanges_52821");
	}

}
