package oz.infra.filter;

public class StringWithinRangeInclusiveFilter {
	private String[] range;

	public StringWithinRangeInclusiveFilter(final String[] range) {
		this.range = range;
	}

	public final boolean accept(final String string) {
		return (string != null && range != null && range.length == 2
				&& string.compareTo(range[0]) >= 0 && string.compareTo(range[1]) <= 0);
	}

}
