package oz.test.infra;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.logging.Logger;

import org.junit.Test;

import oz.infra.io.FolderUtils;
import oz.infra.logging.jul.JulUtils;

public class TestFolderUtils {
	private static final int FILE_COUNT = 7;
	private static Logger logger = JulUtils.getLogger();

	@Test
	public void testCopmareFolders() {
		String folder1Path = "C:\\temp\\test\\infra1";
		String folder2Path = "C:\\temp\\test\\infra2";
		File folder1 = new File(folder1Path);
		File folder2 = new File(folder2Path);
		if (!folder1.exists()) {
			folder1.mkdirs();
		}
		if (!folder2.exists()) {
			folder2.mkdirs();
		}
		System.out.println(FolderUtils.getLatestFile(folder1));
		assertTrue(FolderUtils.compareFoldersByContents(folder1, folder2));
		String filePath = folder1Path + "\\file";
		try {
			for (int i = 0; i < FILE_COUNT; i++) {
				File file = new File(filePath + String.valueOf(i));
				file.createNewFile();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info(String.valueOf(FolderUtils.getFilesInFolder(folder1Path).length)
				+ " created in folder " + folder1Path);
	}
}
