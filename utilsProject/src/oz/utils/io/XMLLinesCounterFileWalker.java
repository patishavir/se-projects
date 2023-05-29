package oz.utils.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.DirectoryWalker;

import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;

public class XMLLinesCounterFileWalker extends DirectoryWalker {
	private static Logger logger = Logger.getLogger(XMLLinesCounterFileWalker.class.getName());
	private static int fileCounter = 0;
	private static int xmlFileCounter = 0;
	private static int xmlFileLineCounter = 0;
	private static ArrayList filesArrayList = new ArrayList();
	private static String fileTypeFilter = "xml";

	public XMLLinesCounterFileWalker() {
		super();
	}

	public List startWalking(File startDirectory) {
		try {
			walk(startDirectory, filesArrayList);
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return filesArrayList;
	}

	protected boolean handleDirectory(File directory, int depth, Collection results) {
		return true;
	}

	protected void handleFile(File file, int depth, Collection results) {
		// delete file and add to list of deleted
		this.filesArrayList.add(file);
		processFile(file);

	}

	public static void main(final String[] args) {
		XMLLinesCounterFileWalker fileWalker = new XMLLinesCounterFileWalker();
		String startDirectoryPath = args[0];
		fileTypeFilter = args[1];
		File startDirectoryFile = new File(startDirectoryPath);
		fileWalker.startWalking(startDirectoryFile);
		if (fileCounter != filesArrayList.size()) {
			logger.warning("File counters do not match !");
		}
		logger.fine(String.valueOf(fileCounter) + " files in " + startDirectoryPath);
		logger.fine(String.valueOf(xmlFileCounter) + " " + fileTypeFilter + " files in "
				+ startDirectoryPath);
		logger.info(String.valueOf(xmlFileLineCounter) + " lines in "
				+ String.valueOf(xmlFileCounter) + " " + fileTypeFilter + " files in "
				+ startDirectoryPath + ".");

	}

	private void processFile(final File file) {
		logger.finest(file.getAbsolutePath());
		fileCounter++;
		String currentFileType = PathUtils.getFileExtension(file);
		logger.finest(currentFileType);
		if (currentFileType.equalsIgnoreCase(fileTypeFilter)) {
			xmlFileCounter++;
			int numberOfLines = FileUtils.getNumberOfLines(file);
			xmlFileLineCounter += numberOfLines;
			logger.finest(file.getAbsolutePath() + "  has " + String.valueOf(numberOfLines)
					+ " lines.  ****************");
		}

	}
}