package oz.infra.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.IOFileFilter;

public class DirectoryWalkerExtender extends DirectoryWalker {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public DirectoryWalkerExtender(final IOFileFilter dirFilter, final IOFileFilter fileFilter,
			final int depth) {
		super(dirFilter, fileFilter, depth);
	}

	protected boolean handleDirectory(File directory, int depth, Collection results) {
		logger.finest("Handle directory: " + directory.getAbsolutePath());
		results.add(directory);
		return true;
	}

	protected void handleFile(File file, int depth, Collection results) {
		// delete file and add to list of deleted
		logger.finest("Handle file: " + file.getAbsolutePath());
		results.add(file);
	}

	public ArrayList<File> startWalking(File startDirectory) {
		ArrayList<File> results = new ArrayList<File>();
		try {
			logger.info("Entering startWalking");
			walk(startDirectory, results);

		} catch (IOException ioe) {
			ioe.printStackTrace();
			logger.warning(ioe.getMessage());
			System.exit(-1);
		}
		return results;
	}

}
