package oz.infra.nio.visitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import oz.infra.exception.ExceptionUtils;
import oz.infra.logging.jul.JulUtils;

/** Recursive listing with SimpleFileVisitor in JDK 7. */
public final class FileVisitorUtils {
	private List<File> fileList = new ArrayList<File>();
	private Logger logger = JulUtils.getLogger();

	public List<File> recursivelyGetFilesOnly(final String rootFolderPath) {
		if (new File(rootFolderPath).isDirectory()) {
			try {
				FileVisitor<Path> fileVisitor = new MyFileVisitor();
				Files.walkFileTree(Paths.get(rootFolderPath), fileVisitor);
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			}
		}
		return fileList;
	}

	public List<File> recursivelyGetFoldersOnly(final String rootFolderPath) {
		if (new File(rootFolderPath).isDirectory()) {
			try {
				// FileVisitor<Path> folderVisitor = new FolderVisitor(null);
				FileVisitor<Path> folderVisitor = new FolderVisitor(rootFolderPath);
				Files.walkFileTree(Paths.get(rootFolderPath), folderVisitor);
			} catch (Exception ex) {
				ExceptionUtils.printMessageAndStackTrace(ex);
			}
		}
		return fileList;
	}

	private final class MyFileVisitor extends SimpleFileVisitor<Path> {
		@Override
		public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException {
			logger.finest("Processing file:" + aFile);
			fileList.add(aFile.toFile());
			return FileVisitResult.CONTINUE;
		}
	}

	private final class FolderVisitor extends SimpleFileVisitor<Path> {
		private String rootFolderPath = null;

		FolderVisitor(final String rootFolderPath) {
			if (rootFolderPath == null) {
				this.rootFolderPath = null;
			} else {
				this.rootFolderPath = new File(rootFolderPath).getAbsolutePath();
			}
		}

		@Override
		public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) throws IOException {
			logger.finest("Processing directory:" + aDir);
			if (rootFolderPath == null || !aDir.toFile().getAbsolutePath().equals(rootFolderPath)) {
				fileList.add(aDir.toFile());
			}
			return FileVisitResult.CONTINUE;
		}
	}
}