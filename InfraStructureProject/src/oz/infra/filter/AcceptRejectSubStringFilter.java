package oz.infra.filter;

import oz.infra.constants.OzConstants;

public class AcceptRejectSubStringFilter implements StringFilter {
	private String subStingFilter = null;
	private AcceptReject operator = null;

	public AcceptRejectSubStringFilter(final String subStingFilter, final String operator) {
		this.subStingFilter = subStingFilter;
		this.operator = AcceptReject.valueOf(operator);
	}

	public final boolean accept(final String string2Filter) {
		boolean rc = true;
		if (subStingFilter != null && subStingFilter.length() > 0 && operator != null) {
			int index = string2Filter.indexOf(subStingFilter);
			rc = (index > OzConstants.STRING_NOT_FOUND && operator.equals(AcceptReject.ACCEPT))
					|| (index == OzConstants.STRING_NOT_FOUND && operator.equals(AcceptReject.REJECT));
		}
		return rc;
	}
}
