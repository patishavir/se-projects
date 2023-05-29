package oz.infra.filter;

import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class AcceptRejectAnySubStringFilter implements StringFilter {
	private String subStingFilter = null;
	private AcceptRejectSubStringFilter[] acceptRejectSubStringFilterArray = new AcceptRejectSubStringFilter[0];
	private AcceptReject operator = null;
	private static String subStringsDelimiter = OzConstants.COMMA;
	private int rejectCounter = 0;
	private static final Logger logger = JulUtils.getLogger();

	public AcceptRejectAnySubStringFilter(final String subStingFilter, final String operator) {
		this.operator = AcceptReject.valueOf(operator);
		if (subStingFilter != null && subStingFilter.length() > 0) {
			this.subStingFilter = subStingFilter;
			String[] subStingFilterArray = this.subStingFilter.split(subStringsDelimiter);
			acceptRejectSubStringFilterArray = new AcceptRejectSubStringFilter[subStingFilterArray.length];
			for (int i = 0; i < subStingFilterArray.length; i++) {
				acceptRejectSubStringFilterArray[i] = new AcceptRejectSubStringFilter(subStingFilterArray[i], operator);
			}
		}
	}

	public final boolean accept(final String string2Filter) {
		boolean rc = true;
		for (AcceptRejectSubStringFilter acceptRejectSubStringFilter : acceptRejectSubStringFilterArray) {
			rc = acceptRejectSubStringFilter.accept(string2Filter);
			if (rc && operator.equals(AcceptReject.ACCEPT)) {
				break;
			}
			if (!rc && operator.equals(AcceptReject.REJECT)) {
				break;
			}
		}
		if (!rc) {
			rejectCounter++;
		}

		String message = StringUtils.concat("string2Filter: ", string2Filter, " operator: ", operator.toString(),
				" rc: ", String.valueOf(rc));
		logger.finest(message);
		return rc;
	}

	public final int getRejectCounter() {
		return rejectCounter;
	}
}
