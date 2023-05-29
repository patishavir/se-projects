package oz.jdir.fileoperations;

import java.awt.Frame;
import java.io.File;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

import oz.guigenerator.GuiGeneratorParamsFileProcessor;
import oz.infra.thread.SynchronizingThread;
import oz.jdir.JdirParameters;
import oz.jdir.abstractfileoperations.AbstractFileContentsModificationOperation;

public class FindAndReplaceOperation extends AbstractFileContentsModificationOperation implements
		Observer {
	private String suffixFilter = "";
	private String string2Find = null;
	private String replaceWithString = null;
	private SynchronizingThread synchronizingThread;

	@Override
	protected String modifyFileContents(String currentFileContents) {
		// TODO Auto-generated method stub
		return currentFileContents.replaceAll(string2Find, replaceWithString);
	}

	@Override
	protected boolean validateFileContents(String currentFileContents, String currentFilePath) {
		// TODO Auto-generated method stub

		return (!(currentFileContents == null));
	}

	@Override
	protected boolean validateFilePath(String currentFilePath, File currentFile) {
		// TODO Auto-generated method stub
		return currentFile.exists() && currentFile.isFile();

	}

	@Override
	protected boolean processParameters() {
		// TODO Auto-generated method stub
		if ((JdirParameters.getString2Find() == null)
				|| JdirParameters.getReplaceWithString() == null
				|| JdirParameters.getSuffixFilter() == null) {
			InputStream replaceStringXmlInputStream = this.getClass().getResourceAsStream(
					JdirParameters.getReplaceStringGuiXmlFile());
			if (replaceStringXmlInputStream == null) {
				logger.warning("load of resource " + JdirParameters.getReplaceStringGuiXmlFile()
						+ " failed.");
			}
			new GuiGeneratorParamsFileProcessor().getParamsXmlDocument(replaceStringXmlInputStream,
					this, null, (Frame) JdirParameters.getDirFrame());
			synchronizingThread = SynchronizingThread.getSynchronizingThread();
			try {
				synchronizingThread.join();
			} catch (InterruptedException iex) {
				logger.info("Thread interrupted");
			}
		}
		string2Find = JdirParameters.getString2Find();
		replaceWithString = JdirParameters.getReplaceWithString();
		suffixFilter = JdirParameters.getSuffixFilter();
		logger.info("string2Replace: " + string2Find + " \tnewString: " + replaceWithString
				+ "\tsuffixFilter: " + suffixFilter);
		return true;
	}

	/*
	 * getSufixFilter
	 */
	protected String getSuffixFilter() {
		return suffixFilter;
	}

	public void update(final Observable Observable, final Object parametersHashTableObj) {
		JdirParameters.update(Observable, parametersHashTableObj);
		logger.finest("About to interrup synchronizing thread!");
		synchronizingThread.interrupt();
		logger.finest("Interruped synchronizing thread!");
	}
}
