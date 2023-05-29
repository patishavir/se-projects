package oz.infra.nio;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyFileVisitor extends SimpleFileVisitor<Path> {
	private final Path targetPath;
	private Path sourcePath = null;
	private int fileCopyCount = 0;
	private CopyOption[] copyOptions = null;

	public CopyFileVisitor(final Path targetPath, final CopyOption... copyOptions) {
		this.targetPath = targetPath;
		this.copyOptions = copyOptions;
	}

	public int getFileCopyCount() {
		return fileCopyCount;
	}

	@Override
	public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
		if (sourcePath == null) {
			sourcePath = dir;
		} else {
			Files.createDirectories(targetPath.resolve(sourcePath.relativize(dir)));
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
		Files.copy(file, targetPath.resolve(sourcePath.relativize(file)), copyOptions);
		fileCopyCount++;
		return FileVisitResult.CONTINUE;
	}
}
