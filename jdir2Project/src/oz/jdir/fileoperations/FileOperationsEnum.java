package oz.jdir.fileoperations;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;

public enum FileOperationsEnum {
	Mirror("Make destination directory identical to source directory", "MirrorOperation",
			FileOperationTypesEnum.twoSidedFileOperation, 2), Synchronize(
			"Synchronize source and destination directories", "SyncOperation",
			FileOperationTypesEnum.twoSidedFileOperation, 2), Print2DirInfo(
			"Print two sided directory information", "Print2FileDirectoryInfoOperation",
			FileOperationTypesEnum.twoSidedFileOperation, 2),
	//
	CopyAll("Copy all files from source to destination directory", "CopyOperation",
			FileOperationTypesEnum.allFilesOperation, 2), MoveAll(
			"Move all files from source to destination directory (Delete file from source after successful copy)",
			"CopyOperation", FileOperationTypesEnum.allFilesOperation, 2), DeleteAll(
			"Delete all files from source directory", "DeleteOperation",
			FileOperationTypesEnum.allFilesOperation, 1), DateAll(
			"Change last modified date for all source directory files",
			"SetFileModifyDateOperation", FileOperationTypesEnum.allFilesOperation, 1), PrintAllDirInfo(
			"Print directory information for all source directory files",
			"PrintFileDirectoryInfoOperation", FileOperationTypesEnum.allFilesOperation, 1), Copy(
			"Copy selected files", "CopyOperation", FileOperationTypesEnum.selectedFilesOperation,
			2), Move("Move selected files", "CopyOperation",
			FileOperationTypesEnum.selectedFilesOperation, 2), Delete("Delete selected files",
			"DeleteOperation", FileOperationTypesEnum.selectedFilesOperation, 1), Rename(
			"Rename selected files", "RenameOperation",
			FileOperationTypesEnum.selectedFilesOperation, 1), ChangeFileType(
			"Change file type for selected files ", "ChangeFileTypeOperation",
			FileOperationTypesEnum.selectedFilesOperation, 1), FindAndReplace(
			"replace all occurances of one string with another string in selected files",
			"FindAndReplaceOperation", FileOperationTypesEnum.selectedFilesOperation, 1), Mkdir(
			"Mkdir in selected directories", "MkdirOperation",
			FileOperationTypesEnum.selectedFilesOperation, 1), SetFileModifyDate(
			"Set file modify date for selected files", "SetFileModifyDateOperation",
			FileOperationTypesEnum.selectedFilesOperation, 1), PrintDirInfo(
			"Print selected files directory info", "PrintFileDirectoryInfoOperation",
			FileOperationTypesEnum.selectedFilesOperation, 1), Open("Open selected files",
			"OpenOperation", FileOperationTypesEnum.selectedFilesOperation, 1), Compare(
			"Compare selected files", "CompareOperation",
			FileOperationTypesEnum.selectedFilesOperation, 2);

	private final String description;
	private String operationClass;
	private final FileOperationTypesEnum operationType;
	private int requiredSides;
	private boolean enabled = true;
	private boolean temporarilyDisabled = false;
	private JButton jButton = null;
	private JMenuItem jMenuItem = null;
	private JRadioButton jRadioButton = null;

	FileOperationsEnum(final String description, final String operationClass,
			final FileOperationTypesEnum operationType, final int requiredSides) {
		this.description = description;
		this.operationClass = operationClass;
		this.operationType = operationType;
		this.requiredSides = requiredSides;
	}

	public String getDescription() {
		return description;
	}

	public FileOperationTypesEnum getoperationType() {
		return operationType;
	}

	public static final int getCount(FileOperationTypesEnum fileOpertaionType) {
		int i = 0;
		for (FileOperationsEnum fileOperation : FileOperationsEnum.values()) {
			if (fileOperation.getoperationType() == fileOpertaionType) {
				i++;
			}
		}
		return i;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getOperationClass() {
		return operationClass;
	}

	public static boolean isSetFileModifyDateOperation(final String actionCommandString) {
		return actionCommandString.equals(FileOperationsEnum.SetFileModifyDate.toString());
	}

	public final JButton getJButton() {
		return jButton;
	}

	public final void setJButton(JButton button) {
		jButton = button;
	}

	public final JMenuItem getJMenuItem() {
		return jMenuItem;
	}

	public final void setJMenuItem(JMenuItem menuItem) {
		jMenuItem = menuItem;
	}

	public final JRadioButton getJRadioButton() {
		return jRadioButton;
	}

	public final void setJRadioButton(JRadioButton radioButton) {
		jRadioButton = radioButton;
		if (temporarilyDisabled && requiredSides == 2) {
			jRadioButton.setEnabled(false);
		}
	}

	public final int getRequiredSides() {
		return requiredSides;
	}

	public final boolean isTemporarilyDisabled() {
		return temporarilyDisabled;
	}

	public final void setTemporarilyDisabled(boolean temporarilyDisabled) {
		this.temporarilyDisabled = temporarilyDisabled;
		disableFileOperationGUI(temporarilyDisabled);
	}

	public final void disableFileOperationGUI(final boolean disableGUI) {
		if (getJButton() != null) {
			getJButton().setEnabled(!disableGUI);
		}
		if (getJMenuItem() != null) {
			getJMenuItem().setEnabled(!disableGUI);
		}
		if (getJRadioButton() != null) {
			getJRadioButton().setEnabled(!disableGUI);
		}

	}
}
