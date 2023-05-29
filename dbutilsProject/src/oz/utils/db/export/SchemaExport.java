package oz.utils.db.export;

import java.util.Properties;

import oz.infra.datetime.StopWatch;
import oz.infra.db.DBUtils;
import oz.infra.string.StringUtils;

public class SchemaExport {

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		SchemaExportParameters.processParameters(args[0]);
		StopWatch stopWatch = new StopWatch();
		for (String database : SchemaExportParameters.getDatabasesArray()) {
			StopWatch dbStopWatch = new StopWatch();
			Properties properties = SchemaExportParameters.getDatabaseProperties(database);
			DBUtils.exportSchema(properties);
			dbStopWatch.logElapsedTimeMessage(StringUtils.concat(database, " processing has completed in"));
		}
		stopWatch.logElapsedTimeMessage("All databases processing has completed in ");
		SchemaExportUtils.startCompareTool();
	}

}
