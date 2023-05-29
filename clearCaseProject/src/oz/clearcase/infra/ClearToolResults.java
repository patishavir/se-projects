package oz.clearcase.infra;

public class ClearToolResults {
	private int returnCode;
	private String stdOut;
	private String stdErr;

	public ClearToolResults(final int returnCodeP, final String stdOutP, final String stdErrP) {
		this.returnCode = returnCodeP;
		this.stdOut = stdOutP;
		this.stdErr = stdErrP;
	}

	public final int getReturnCode() {
		return returnCode;
	}

	public final String getStdErr() {
		return stdErr;
	}

	public final String getStdOut() {
		return stdOut;
	}
}
