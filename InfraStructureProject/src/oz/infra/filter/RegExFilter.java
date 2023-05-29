package oz.infra.filter;

import oz.infra.regexp.RegexpUtils;

public class RegExFilter implements StringFilter {
	private String regExpression = null;

	public RegExFilter(final String regEx) {
		this.regExpression = regEx;
	}

	public boolean accept(final String string2Filter) {
		return RegexpUtils.matches(string2Filter, regExpression);
	}
}
