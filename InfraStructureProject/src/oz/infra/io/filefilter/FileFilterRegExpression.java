package oz.infra.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.varargs.VarArgsUtils;

public class FileFilterRegExpression implements FileFilter {
	private String regexpression = null;
	private boolean verbose = false;
	private static Logger logger = JulUtils.getLogger();

	public final boolean accept(final File file) {
		boolean result = RegexpUtils.matches(file.getAbsolutePath(), regexpression);
		String acceptReject = "accept";
		if (!result) {
			acceptReject = "reject";
		}
		if (verbose) {
			logger.info(
					file.getAbsolutePath() + " has been " + acceptReject + "ed. Regexpression filer: " + regexpression);
		}
		return result;
	}

	public FileFilterRegExpression(final String regexpression, Boolean... verboseParam) {
		this.regexpression = regexpression;
		verbose = VarArgsUtils.getMyArg(false, verboseParam);
	}
}