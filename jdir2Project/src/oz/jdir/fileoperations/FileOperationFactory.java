package oz.jdir.fileoperations;

import java.util.logging.Logger;

import oz.jdir.abstractfileoperations.AbstractFileOperation;

public final class FileOperationFactory {
	private static FileOperationFactory fileOperationFactory = new FileOperationFactory();
	private Logger logger = Logger.getLogger(this.getClass().toString());

	private FileOperationFactory() {
	}

	public final AbstractFileOperation getFileOperation(final String actionCommand) {
		AbstractFileOperation myFileOperation = null;
		FileOperationsEnum fileOperation1 = FileOperationsEnum.valueOf(actionCommand);
		String myPackage = this.getClass().getPackage().getName();
		try {
			myFileOperation = (AbstractFileOperation) (Class.forName(myPackage + "."
					+ fileOperation1.getOperationClass()).newInstance());
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			ex.printStackTrace();
		}
		return myFileOperation;
	}

	public static final FileOperationFactory getFileOperationFactory() {
		return fileOperationFactory;
	}
}