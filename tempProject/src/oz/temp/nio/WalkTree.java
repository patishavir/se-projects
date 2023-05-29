package oz.temp.nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class WalkTree {
	public static void main(String[] args) throws IOException {
		System.out.println("starting ...");
		Path start = FileSystems.getDefault().getPath("C:/temp");
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
				File file1 = file.toFile();
				if (file1.isFile()) {
					System.out.println(file + "FFFFFFFFFFFFFFFFFFFFF");
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(final Path file, final IOException ioe) throws IOException {
				System.out.println(file.toString());
				return FileVisitResult.CONTINUE;
			}
		});
	}
}