/*
 * Created on 30/08/2005 @author utxet06
 */
package oz.infra.clearcase;

public class ClearCaseAttributes {
	private String fileFullPath = null;
	private boolean isCheckedOut = false;
	private boolean isCCmaganaged = false;
	private boolean isHijacked = false;
	private boolean isFromFoundationBaseline = false;

	public final String getFileFullPath() {
		return fileFullPath;
	}

	public final void setFileFullPath(final String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}

	public final boolean isCheckedOut() {
		return isCheckedOut;
	}

	public final void setCheckedOut(final boolean isCcheckedOut) {
		this.isCheckedOut = isCcheckedOut;
	}

	public final boolean isCCmaganaged() {
		return isCCmaganaged;
	}

	public final void setCCmaganaged(final boolean isCCmaganaged) {
		this.isCCmaganaged = isCCmaganaged;
	}

	public final boolean isFromFoundationBaseline() {
		return isFromFoundationBaseline;
	}

	public final void setFromFoundationBaseline(final boolean isFromFoundationBaseline) {
		this.isFromFoundationBaseline = isFromFoundationBaseline;
	}

	public final boolean isHijacked() {
		return isHijacked;
	}

	public final void setHijacked(final boolean isHijacked) {
		this.isHijacked = isHijacked;
	}
}