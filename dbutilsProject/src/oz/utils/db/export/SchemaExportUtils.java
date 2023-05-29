package oz.utils.db.export;

import java.io.File;

import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.run.processbuilder.SystemCommandExecutorRunner;

public class SchemaExportUtils {
	public static void startCompareTool() {
		String compareToolPath = SchemaExportParameters.getCompareToolPath();
		if (compareToolPath != null) {
			File compareToolFile = new File(compareToolPath);
			if (compareToolFile.isFile()) {
				String folderPath = SchemaExportParameters.getFolderPath();
				String[] parametersArray1 = { compareToolPath, folderPath, folderPath };
				SystemCommandExecutorResponse systemCommandExecutorResponse1 = SystemCommandExecutorRunner
						.run(parametersArray1);
			}
		}
	}
}
