package oz.infra.io.filefilter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.string.StringUtils;

public class FileNameFilterRegExpression implements FilenameFilter {
	private String regexpression = null;
	private static Logger logger = JulUtils.getLogger();

	public final boolean accept(final File file, final String name) {
		boolean match = RegexpUtils.matches(name, regexpression);
		logger.info(
				StringUtils.concat("match: ", String.valueOf(match), " name: ", name, " file:", file.getAbsolutePath()));
		return match;
	}

	public FileNameFilterRegExpression(final String regexpression) {
		this.regexpression = regexpression;
	}
}
