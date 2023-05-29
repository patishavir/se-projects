package oz.infra.io;

public interface LineProcessor {
	public void processLine(final String line);

	public void processEOF(Object object);
}
