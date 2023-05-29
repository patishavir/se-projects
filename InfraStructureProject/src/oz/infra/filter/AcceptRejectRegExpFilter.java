package oz.infra.filter;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.debug.DebugUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class AcceptRejectRegExpFilter implements StringFilter {

	private String regExpFilterString = null;
	private AllAny allAny = AllAny.ALL;
	private static final String TOP_LEVEL_DELIMITER = OzConstants.COLON;
	private static final String ENTRY_DELIMITER = OzConstants.HASH;
	private static String PAIR_DELIMITER = OzConstants.COMMA;
	private int rejectedStringsCounter = 0;
	private String[] regExpFiltersArray = null;
	private Boolean defaultVerdict = false;
	private static boolean debugMode = DebugUtils.isDebugMode();
	private static final Logger logger = JulUtils.getLogger();

	public AcceptRejectRegExpFilter(final String regExpFilterString) {
		this.regExpFilterString = regExpFilterString;
		String[] regExpFilterArray1 = regExpFilterString.split(TOP_LEVEL_DELIMITER);
		String acceptRejectAll = regExpFilterArray1[0];
		if (acceptRejectAll.startsWith(AcceptReject.ACCEPT.toString())) {
			defaultVerdict = true;
		} else {
			if (acceptRejectAll.startsWith(AcceptReject.REJECT.toString())) {
				defaultVerdict = false;
			} else {
				SystemUtils.printMessageAndExit(acceptRejectAll + " is invalid!", OzConstants.EXIT_STATUS_ABNORMAL,
						false);
			}
		}
		if (regExpFilterArray1[1].endsWith(AllAny.ANY.toString())) {
			allAny = AllAny.ANY;
		}
		regExpFiltersArray = regExpFilterArray1[2].split(ENTRY_DELIMITER);
	}

	public final boolean accept(final String string2Filter) {
		int acceptCount = 0;
		int rejectCount = 0;
		int acceptFailedCount = 0;
		int rejectFailedCount = 0;
		int filterCount = regExpFiltersArray.length;
		for (String regExpFilter1 : regExpFiltersArray) {
			String[] regExpFilter1Array = regExpFilter1.split(PAIR_DELIMITER);
			AcceptReject acceptReject = AcceptReject.valueOf(regExpFilter1Array[0]);
			String regExp1 = regExpFilter1Array[1];
			logger.finest("regExp1: " + regExp1);
			RegExFilter regExFilter = new RegExFilter(regExp1);
			boolean regexpMatched = regExFilter.accept(string2Filter);
			if (regexpMatched && acceptReject.equals(AcceptReject.ACCEPT)) {
				acceptCount++;
			}
			if (!regexpMatched && acceptReject.equals(AcceptReject.ACCEPT)) {
				acceptFailedCount++;
			}
			if (regexpMatched && acceptReject.equals(AcceptReject.REJECT)) {
				rejectCount++;
			}
			if (!regexpMatched && acceptReject.equals(AcceptReject.REJECT)) {
				rejectFailedCount++;
			}

		}
		boolean verdict = defaultVerdict;
		if (defaultVerdict) {
			if (allAny.equals(AllAny.ALL)) {
				verdict = ((rejectCount == 0) && (acceptFailedCount == 0));
			} else {
				verdict = ((acceptFailedCount == 0) && (rejectCount == 0));
			}
		} else {
			if (allAny.equals(AllAny.ALL)) {
				verdict = (acceptCount > 0) && (acceptCount + rejectFailedCount == filterCount);
			} else {
				verdict = ((acceptCount > 0) && (rejectCount == 0));
			}
		}
		if (debugMode) {
			// String message1 = "\n\n\nacceptCount: " +
			// String.valueOf(acceptCount) + " rejectCount: "
			// + String.valueOf(rejectCount) + " acceptFailedCount: " +
			// String.valueOf(acceptFailedCount)
			// + " rejectFailedCount: " + String.valueOf(rejectFailedCount) + "
			// filterCount: "
			// + String.valueOf(filterCount);
			// String message2 = StringUtils.concat("input:\t", string2Filter,
			// "\nfilter:\t", regExpFilterString,
			// "\nverdict:\t", String.valueOf(verdict));
			// System.out.println(message1 + message2);
		}
		if (verdict && debugMode) {
			logger.info(string2Filter);
		}
		if (!verdict) {
			rejectedStringsCounter++;
		}
		logger.finest("string2Filter: " + string2Filter + "rejectCounter: " + String.valueOf(rejectedStringsCounter));
		return verdict;
	}

	public final int getRejectedStringsCounter() {
		return rejectedStringsCounter;
	}
}
