package oz.utils.cm.ds.deployment.stats;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.operaion.Outcome;
import oz.infra.system.SystemUtils;

public class DsDeploymentStats {
	private String commands = null;
	private int successfulImports = 0;
	private int unsuccessfulImports = 0;
	private static final String LS = SystemUtils.LINE_SEPARATOR;
	private static final Logger logger = JulUtils.getLogger();

	public DsDeploymentStats(final String commands) {
		this.commands = commands;
	}

	public final StringBuilder getStatsReport() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(successfulImports) + " " + commands + " have completed successfully." + LS);
		sb.append(String.valueOf(unsuccessfulImports) + " " + commands + " have failed." + LS);
		return sb;
	}

	public int getSuccessfulImports() {
		return successfulImports;
	}

	public final void printStatsReport() {
		logger.info(getStatsReport().toString());
	}

	public final void updateRunStats(final Outcome outcome) {
		if (outcome == Outcome.SUCCESS) {
			successfulImports++;
		} else {
			unsuccessfulImports++;
		}
	}
}
