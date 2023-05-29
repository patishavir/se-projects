package oz.utils.files;

import oz.infra.io.FolderUtils;
import oz.infra.logging.jul.JulUtils;

public class FolderUtilsMain {
	private enum FolderUtilsEnum {
		CopySelectedFiles2Folder
	};

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JulUtils.addFileHandlerFromEnv(true);
		FolderUtilsEnum folderUtilsEnum = FolderUtilsEnum.valueOf(args[0]);
		String sourceFolder = args[1];
		String rangeFilter = args[2];
		String targetFolder = args[3];
		switch (folderUtilsEnum) {
		case CopySelectedFiles2Folder:
			FolderUtils.copySelectedFiles2Folder(sourceFolder, rangeFilter, targetFolder);
		}
	}
}
