package oz.infra.filter;

public class StringWithinRangeFilter {
	private String[] range;

	public StringWithinRangeFilter(final String[] range) {
		this.range = range;
	}

	public boolean accept(final String string) {
		return (string != null && range != null && range.length == 2
				&& string.compareTo(range[0]) > 0 && string.compareTo(range[1]) < 0);
	}
}
