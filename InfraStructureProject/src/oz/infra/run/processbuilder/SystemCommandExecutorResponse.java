package oz.infra.run.processbuilder;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class SystemCommandExecutorResponse {
	private static final Logger logger = JulUtils.getLogger();
	private static final String LINE_SEPARATOR = SystemUtils.LINE_SEPARATOR;
	private int returnCode = Integer.MIN_VALUE;
	private String stdout = null;
	private String stderr = null;

	public SystemCommandExecutorResponse(final int returnCode, final String stdout, final String stderr) {
		this.returnCode = returnCode;
		this.stdout = stdout;
		this.stderr = stderr;
	}

	public final int getReturnCode() {
		return returnCode;
	}

	public final String getStderr() {
		return stderr;
	}

	public final String getStdout() {
		return stdout;
	}

	public final String getExecutorResponse() {
		return getExecutorResponse(true, true);
	}

	public final String getExecutorResponse(final boolean trim, final boolean includeStdout) {
		StringBuilder sb = new StringBuilder(LINE_SEPARATOR);
		sb.append("returnCode: ".concat(String.valueOf(returnCode)));
		sb.append(LINE_SEPARATOR);
		if (includeStdout) {
			sb.append("stdout:".concat(LINE_SEPARATOR));
			if (stdout != null && stdout.trim().length() > 0) {
				if (trim) {
					String[] trimmedArray = StringUtils.trimArray(stdout.split(LINE_SEPARATOR));
					sb.append(StringUtils.stringArray2String(trimmedArray, LINE_SEPARATOR));
				} else {
					sb.append(stdout);
				}
				if (sb.charAt(sb.length() - 1) != LINE_SEPARATOR.charAt(0)) {
					sb.append(LINE_SEPARATOR);
					sb.append(LINE_SEPARATOR);
				}
			}
		}
		sb.append("stderr:".concat(LINE_SEPARATOR));
		if (stderr != null) {
			if (trim) {
				String[] trimmedArray = StringUtils.trimArray(stderr.split(LINE_SEPARATOR));
				sb.append(StringUtils.stringArray2String(trimmedArray, LINE_SEPARATOR));
			} else {
				sb.append(stderr);
			}
		}
		return sb.toString();
	}
}
