package oz.infra.nio.visitor;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class FilesInFolderVisitor extends SimpleFileVisitor<Path> {
	private static Logger logger = JulUtils.getLogger();
	private List<String> fileList = new ArrayList<String>();

	@Override
	public final FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs) {
		logger.info("************* path: " + path.toString());
		fileList.add(path.toString());
		return FileVisitResult.CONTINUE;
	}

	public final List<String> getFileList() {
		return fileList;
	}
}
