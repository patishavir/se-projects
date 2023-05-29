package oz.infra.io.filefilter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.string.StringUtils;
import oz.infra.string.WithinRangeEnum;

public class FileNameRegExpresionAndNameRange implements FilenameFilter {
	private static Logger logger = JulUtils.getLogger();
	private String regExpression = null;
	private String fileNameRange = null;
	private WithinRangeEnum[] withinRangeEnums = null;

	public FileNameRegExpresionAndNameRange(final String suffixRegExpression, final String fileNameRange,
			final WithinRangeEnum... withinRangeEnums) {
		this.regExpression = suffixRegExpression;
		this.fileNameRange = fileNameRange;
		this.withinRangeEnums = withinRangeEnums;

	}

	public boolean accept(final File dir, final String fileName) {
		boolean regexpOk = RegexpUtils.matches(fileName, regExpression);
		boolean fileNameRangeOk = true;
		if (regexpOk && fileNameRange != null) {
			fileNameRangeOk = StringUtils.isStringWithinRage(fileName, fileNameRange, withinRangeEnums);
		}
		boolean retCode = regexpOk && fileNameRangeOk;
		if (!retCode) {
			logger.warning(fileName + " will not be processed. suffixRegExpression: " + regExpression
					+ " fileNameRange: " + fileNameRange);
		}
		return retCode;
	}
}
