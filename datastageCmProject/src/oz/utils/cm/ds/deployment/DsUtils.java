package oz.utils.cm.ds.deployment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import oz.infra.collection.CollectionUtils;
import oz.infra.io.FileUtils;
import oz.infra.regexp.RegexpUtils;

public class DsUtils {

	public static List<String> getJobNames(final String dsxFilePath) {
		return getJobNames(new File(dsxFilePath));
	}

	static ArrayList<String> getJobNames(final File dsxFile) {
		final String BEGIN_DSJOB = "BEGIN DSJOB";
		final String Identifier = "Identifier";
		ArrayList<String> jobNames = new ArrayList<>();
		String[] dsxFileStringArray = FileUtils.readTextFile2Array(dsxFile);

		for (int i = 0; i < dsxFileStringArray.length; i++) {
			if (dsxFileStringArray[i].trim().equals(BEGIN_DSJOB)) {
				for (; i < dsxFileStringArray.length; i++) {
					if (dsxFileStringArray[i + 1].trim().startsWith(Identifier)) {
						String[] lineBreakdown = dsxFileStringArray[i + 1].split(RegexpUtils.REGEXP_WHITE_SPACE);
						String jobName = lineBreakdown[lineBreakdown.length - 1].substring(1, lineBreakdown[lineBreakdown.length - 1].length() - 1);
						jobNames.add(jobName);
						break;
					}
				}
			}
		}
		CollectionUtils.printCollection(jobNames, "Job names:\n", Level.INFO);
		return jobNames;
	}
}