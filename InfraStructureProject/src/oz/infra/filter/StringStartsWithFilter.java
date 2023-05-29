package oz.infra.filter;

public class StringStartsWithFilter implements StringFilter {

	private String startString = null;

	public StringStartsWithFilter(final String startString) {
		this.startString = startString;
	}

	public final boolean accept(final String string2Filter) {
		return string2Filter.startsWith(startString);
	}
}
