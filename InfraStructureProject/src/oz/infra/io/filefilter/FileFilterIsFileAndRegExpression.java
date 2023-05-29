package oz.infra.io.filefilter;

import java.io.File;
import java.io.FileFilter;

import oz.infra.regexp.RegexpUtils;
import oz.infra.varargs.VarArgsUtils;

public class FileFilterIsFileAndRegExpression implements FileFilter {
	private String regexpression = null;
	private Boolean pathToLowerCase = true;

	public final boolean accept(final File file) {
		String absolutePath = file.getAbsolutePath();
		if (pathToLowerCase) {
			absolutePath = absolutePath.toLowerCase();
		}
		return file.isFile() && RegexpUtils.matches(absolutePath, regexpression);
	}

	public FileFilterIsFileAndRegExpression(final String regexpression, final Boolean... pathToLowerCaseParam) {
		this.regexpression = regexpression;
		this.pathToLowerCase = VarArgsUtils.getMyArg(Boolean.TRUE, pathToLowerCaseParam);
	}
}
