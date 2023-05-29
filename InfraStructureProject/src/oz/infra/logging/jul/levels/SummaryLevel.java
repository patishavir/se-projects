package oz.infra.logging.jul.levels;

import java.util.logging.Level;

public class SummaryLevel extends Level {
	public static final SummaryLevel SUMMARY = new SummaryLevel();
	public SummaryLevel() {
		super("Summary", Level.INFO.intValue() + 1);
	}
}