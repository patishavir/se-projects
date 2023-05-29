package oz.clearcase.stream.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.apache.commons.collections.ListUtils;

import oz.clearcase.infra.ClearCaseUtils;
import oz.clearcase.infra.ClearToolCommand;
import oz.clearcase.infra.ClearToolResults;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class FoundationBaseLines {
	private static Logger logger = JulUtils.getLogger();

	static final void copy(final String stream1Name, final String stream2Name) {
		String pvob1 = ClearCaseUtils.getStreamPvob(stream1Name);
		if (pvob1 == null) {
			terminate("Could not determine pvob for " + stream1Name
					+ ". Processing has been terminated!");
		}
		String pvob2 = ClearCaseUtils.getStreamPvob(stream2Name);
		if (pvob2 == null) {
			terminate("Could not determine pvob for " + stream2Name
					+ ". Processing has been terminated!");
		}
		String stream1FullName = stream1Name + "@" + pvob1;
		String stream2FullName = stream2Name + "@" + pvob2;
		if (!ClearCaseUtils.isStreamReadOnly(stream2FullName)) {
			terminate("Target stream must be read only! Processing has been terminated.");
		}
		String[] stream1Baselines = ClearCaseUtils
				.getStreamFoundationBaselines(stream1FullName);
		StringUtils.printStringArray(stream1Baselines, "\n" + stream1FullName
				+ " baselines:");
		String[] stream2Baselines = ClearCaseUtils
				.getStreamFoundationBaselines(stream2FullName);
		StringUtils.printStringArray(stream2Baselines, "\n" + stream2FullName
				+ " baselines:");
		List<String> Stream1BaselineList = Arrays.asList(stream1Baselines);
		List<String> Stream2BaselineList = Arrays.asList(stream2Baselines);
		//
		List<String> baseline4RebaseList = ListUtils.subtract(
				Stream1BaselineList, Stream2BaselineList);
		List<String> baseline2RemoveList = ListUtils.subtract(
				Stream2BaselineList, Stream1BaselineList);
		//
		String baseline4RebaseString = getListAsString(baseline4RebaseList);
		String baseline2RemoveString = getListAsString(baseline2RemoveList);
		if (baseline4RebaseString.length() == 0
				&& baseline2RemoveString.length() == 0) {
			terminate("Streams configurations are identical. Rebase has not been performed !");
		}
		//
		String[] viewTags = ClearCaseUtils.getStreamViewTags(stream2FullName);
		if (viewTags == null) {
			terminate("No views for : " + stream2FullName
					+ ". Rebase cannot be performed!");
		}
		StringBuilder clearToolParamsStringBuilder = new StringBuilder();
		char delimiter = '%';
		clearToolParamsStringBuilder.append(" " + delimiter + "rebase");
		if (baseline4RebaseString.length() > 0) {
			clearToolParamsStringBuilder.append(delimiter + "-baseline"
					+ delimiter + baseline4RebaseString);
		}
		if (baseline2RemoveString.length() > 0) {
			clearToolParamsStringBuilder.append(delimiter + "-dbaseline"
					+ delimiter + baseline2RemoveString);

		}
		clearToolParamsStringBuilder.append(delimiter + "-view" + delimiter
				+ viewTags[0] + delimiter + "-stream" + delimiter
				+ stream2FullName + delimiter + "-complete");
		String[] clearToolparams = clearToolParamsStringBuilder.toString()
				.split(String.valueOf(delimiter));
		clearToolparams[0] = null;
		logger.info("rebase command: "
				+ clearToolParamsStringBuilder.toString().replace(delimiter,
						' '));
		ClearToolCommand clearToolCommand = new ClearToolCommand();
		ClearToolResults clearToolResults = clearToolCommand
				.runClearToolCommand(clearToolparams, true);
		logger.info(clearToolResults.getStdOut());
		if (clearToolResults.getStdErr().length() > 0) {
			logger.info("Err: " + clearToolResults.getStdErr());
		}
		if (clearToolResults.getReturnCode() != 0) {
			logger.info("Return code: "
					+ String.valueOf(clearToolResults.getReturnCode()));
		}

	}

	private static final String getListAsString(List<String> myList) {
		if (myList.size() == 0) {
			return "";
		} else {
			StringBuilder stringBuilder = new StringBuilder();
			for (String s1 : myList) {
				stringBuilder.append(s1 + ",");
			}

			return stringBuilder.toString().substring(0,
					stringBuilder.toString().length() - 1);
		}
	}

	private static final void terminate(final String errorMessage) {
		logger.severe(errorMessage);
		JOptionPane.showMessageDialog(null, errorMessage,
				"Copy foundation baselines failed", JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
	}
}
