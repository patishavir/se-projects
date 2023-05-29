package oz.infra.nio.visitor.test;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

import oz.infra.list.ListUtils;
import oz.infra.nio.visitor.FileVisitorUtils;
import oz.infra.system.SystemUtils;

public class FileVisitorUtilsTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<File> fileList = new FileVisitorUtils().recursivelyGetFilesOnly("c:\\temp\\test\\");
		ListUtils.getAsDelimitedString(fileList, SystemUtils.LINE_SEPARATOR, Level.INFO);
		List<File> folderList = new FileVisitorUtils().recursivelyGetFoldersOnly("c:\\temp\\test\\");
		ListUtils.getAsDelimitedString(folderList, SystemUtils.LINE_SEPARATOR, Level.INFO);
	}

}
