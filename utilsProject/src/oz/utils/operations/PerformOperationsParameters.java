package oz.utils.operations;

import oz.infra.constants.OzConstants;
import oz.infra.io.PathUtils;

public class PerformOperationsParameters {
	private String operations2Perform = null;
	private String[] operations2PerformArray = null;
	private String propertiesFolderPath = null;
	private String rootFolderPath = null;

	public final String asString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\noperations2Perform: " + operations2Perform);
		sb.append("\npropertiesFolderPath: " + propertiesFolderPath);
		sb.append("\nrootFolderPath: " + rootFolderPath);
		return sb.toString();
	}

	public final String[] getOperations2PerformArray() {
		return operations2PerformArray;
	}

	public final String getPropertiesFolderPath() {
		return propertiesFolderPath;
	}

	public final void setOperations2Perform(final String operations2Perform) {
		operations2PerformArray = operations2Perform.split(OzConstants.COMMA);
	}

	public final void setPropertiesFolderPath(final String propertiesFolderPath) {
		this.propertiesFolderPath = PathUtils.getFullPath(rootFolderPath, propertiesFolderPath);
	}

	public final void setRootFolderPath(final String rootFolderPath) {
		this.rootFolderPath = rootFolderPath;
	}
}
