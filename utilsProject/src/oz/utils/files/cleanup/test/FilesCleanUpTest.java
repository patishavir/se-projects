package oz.utils.files.cleanup.test;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.nio.NioUtils;

public class FilesCleanUpTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		String[] folderPaths = { "C:\\temp\\tree\\sub10\\sub20\\sub30\\sub40", "C:\\temp\\tree\\sub11\\sub21\\sub31\\sub41",
				"C:\\temp\\tree\\sub12\\sub22\\sub32\\sub42", "C:\\temp\\tree\\sub13\\sub23\\sub33\\sub43",
				"C:\\temp\\tree\\sub14\\sub24\\sub34\\sub44\\sub54\\sub64" };
		NioUtils.createDirectoryTrees(folderPaths);
	}
}