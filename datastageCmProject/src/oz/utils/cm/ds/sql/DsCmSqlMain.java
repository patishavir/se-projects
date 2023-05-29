package oz.utils.cm.ds.sql;

import oz.utils.cm.ds.sql.run.DsCmSqlRun;

public class DsCmSqlMain {

	public static void main(final String[] args) {
		DsCmSqlParameters.processParameters(args[0]);
		DsCmSqlRun.run();
	}
}