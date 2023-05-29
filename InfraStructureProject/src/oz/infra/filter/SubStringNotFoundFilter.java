package oz.infra.filter;

import oz.infra.constants.OzConstants;

public class SubStringNotFoundFilter implements StringFilter {
	private String subString = null;

	public SubStringNotFoundFilter(final String subString2Exclude) {
		this.subString = subString2Exclude;
	}

	public boolean accept(final String string2Filter) {
		return string2Filter.indexOf(subString) == OzConstants.STRING_NOT_FOUND;
	}

}
