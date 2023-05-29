package oz.infra.nio.test;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.exception.ExceptionUtils;
import oz.infra.io.FileUtils;
import oz.infra.io.FolderUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.NioUtils;
import oz.infra.operaion.Outcome;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class TestNioUtils {
	private static Logger logger = JulUtils.getLogger();

	private static FileSystem getZipFileSystem() {
		FileSystem fileSystem = null;
		try {
			String zipFilePath = "C:\\temp\\nio\\test.zip";
			fileSystem = NioUtils.getZipFileSystem(zipFilePath, false);
			logger.info(fileSystem.toString());
			NioUtils.list(fileSystem.getPath("/"), false);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return fileSystem;
	}

	public static void main(final String[] args) {
		// testListFileSystemProviders();
		// getZipFileSystem();
		// testCopy();
		// testWrite();
		// testCreateDirectories();
		// testUpdate();
		// testRecursivelyListFilesInFolder("C:\\temp\\");
		// testRecursivelyListFilesInFolder( "C:\\temp\\logs115",
		// FileSystemFilterEnum.filesOnly);
		// testRecursivelyListFilesInFolder("C:\\temp\\test");
		testCopyAndSaveExisting();
		// testCopy2();
		// testFileMove();
		// testSetLastModifiedTime();
		// testSetLastModifiedTime2();

	}

	private static void testCopy() {
		String source = "c:\\temp\\source.txt";
		String destination = "c:\\temp\\destination.txt";
		NioUtils.copy(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
		NioUtils.copy(Paths.get(source), Paths.get(destination));
		String contents = NioUtils.readFile2String(destination);
		logger.info(contents);
	}

	private static void testCopy2() {
		String source = "c:\\temp\\source.txt";
		String destination = "c:\\temp\\source.txt";
		NioUtils.copy(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
		NioUtils.copy(Paths.get(source), Paths.get(destination));
		String contents = NioUtils.readFile2String(destination);
		logger.info(contents);
	}

	private static void testCopyAndSaveExisting() {
		NioUtils.copyAndSaveExisting("c:\\temp\\niotest\\fin.txt", "c:\\temp\\niotest\\fout.txt");
		NioUtils.copyAndSaveExisting("c:\\temp\\niotest\\fin.txt", "c:\\temp\\niotest\\fout.txt");
	}

	private static void testCopyFolder() {
		String targetFolder = "C:\\temp\\1110NEW12";
		FolderUtils.createFolderIfNotExists(targetFolder);
		NioUtils.copyFolder("C:\\temp\\1110", targetFolder, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
	}

	private static void testCreateDirectories() {
		NioUtils.createDirectory("c:\\temp\\single");
		NioUtils.createDirectories("c:\\temp\\a\\b\\c\\789");
	}

	private static void testFileCopy() {
		String file2CopyPath = "c:\\temp\\file2Copy.txt";
		String folderCopyPath = "c:\\temp\\folder2Copy";
		String targetFilePath = "c:\\temp\\target\\copied.txt";
		String targetFolderPath = "c:\\temp\\targetfolder";
		FileUtils.writeFile(file2CopyPath, "contentsString: + file2Copy.txt");
		NioUtils.copy(file2CopyPath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
		File targetfolder2 = new File(targetFolderPath + "2");
		targetfolder2.mkdirs();
		NioUtils.copy(folderCopyPath, targetFolderPath, StandardCopyOption.REPLACE_EXISTING);
		logger.info("all done");
	}

	private static void testFileMove() {
		String file2MovePath = "c:\\temp\\file2Move.txt";
		String targetFilePath = "c:\\temp\\target\\moved.txt";
		FileUtils.writeFile(file2MovePath, "contentsString: + file2Move.txt");
		NioUtils.move(file2MovePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
		NioUtils.move(file2MovePath, targetFilePath, true, StandardCopyOption.REPLACE_EXISTING);
		logger.info("all done");
	}

	private static void testFolderMove() {
		String folder2MovePath = "c:\\temp\\folder2Move";
		File folder2MoveFile = new File(folder2MovePath);
		folder2MoveFile.mkdir();
		String file2MovePath = "c:\\temp\\folder2Move\\file2Move.txt";
		String targetFolderPath = "c:\\temp\\target\\targetFolder";
		FileUtils.writeFile(file2MovePath, "contentsString: + file2Move.txt");
		NioUtils.move(folder2MovePath, targetFolderPath, StandardCopyOption.REPLACE_EXISTING);
		logger.info("all done");
	}

	private static void testListFileSystemProviders() {
		NioUtils.listFileSystemProviders();
	}

	private static void testPrintStandardCopyOptions() {
		NioUtils.printStandardCopyOptions();
	}

	private static void testRecursivelyListFilesInFolder(final String rootFolderPath) {
		List<String> fileList = NioUtils.recursivelyListFilesInFolder(rootFolderPath);
		logger.info(StringUtils.concat(rootFolderPath, " has ", String.valueOf(fileList.size()), " files."));
		ListUtils.getAsDelimitedString(fileList, SystemUtils.LINE_SEPARATOR, Level.INFO);
	}

	private static void testSetLastModifiedTime() {
		String filePath = "c:/temp";
		String dateFormateString = "MM/dd/yyyy HH:mm:ss";
		String dateIime = "11/31/2016 23:24:25";
		Outcome outcome = NioUtils.setLastModifiedTime(filePath, dateIime, dateFormateString);
		logger.info(outcome.toString());
		logger.info(NioUtils.getLastModifiedTime(filePath, dateFormateString));
	}

	private static void testSetLastModifiedTime2() {
		String filePath = "c:/temp/cleanupTest/28122016";
		String dateFormateString = "dd/MM/yyyy HH:mm:ss";
		String dateIime = "11/04/2017 23:24:25";
		Outcome outcome = NioUtils.setLastModifiedTime(filePath, dateIime, dateFormateString);
		logger.info(outcome.toString());
		logger.info(NioUtils.getLastModifiedTime(filePath, dateFormateString));
	}

	private static void testUpdate() {
		try {
			FileSystem fileSystem = getZipFileSystem();
			NioUtils.delete(fileSystem.getPath("/sub/4.txt"));
			NioUtils.list(fileSystem.getPath("/"), false);
			// NioUtils.copy(Paths.get("C:\\temp\\nio\\4.txt"),
			// fileSystem.getPath("/sub/4.txt"));
			// NioUtils.list(fileSystem.getPath("/"), false);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	private static void testUpdate1() {
		try {
			FileSystem fileSystem = getZipFileSystem();
			NioUtils.delete(fileSystem.getPath("/sub/4.txt"));
			NioUtils.list(fileSystem.getPath("/"), false);
			// NioUtils.copy(Paths.get("C:\\temp\\nio\\4.txt"),
			// fileSystem.getPath("/sub/4.txt"));
			// NioUtils.list(fileSystem.getPath("/"), false);
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
	}

	private static void testWrite() {
		String filePath = "c:\\temp\\out.txt";
		String contents = "111\n222\n333\n";
		if (new File(filePath).exists()) {
			NioUtils.writeString2File(filePath, contents, StandardOpenOption.APPEND);
		} else {
			NioUtils.writeString2File(filePath, contents, StandardOpenOption.CREATE);
		}
		String curretContents = NioUtils.readFile2String(filePath);
		logger.info(curretContents);
	}
}
