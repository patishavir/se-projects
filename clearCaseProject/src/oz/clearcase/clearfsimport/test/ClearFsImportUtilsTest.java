package oz.clearcase.clearfsimport.test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import oz.clearcase.clearfsimport.ClearFsImportUtils;
import oz.infra.logging.jul.JulUtils;

public class ClearFsImportUtilsTest {
	private static Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testGetClearfsimportPath();
		testRunClearfsimport();
	}

	private static void testGetClearfsimportPath() {
		logger.info(ClearFsImportUtils.getClearfsimportPath());
	}

	private static void testRunClearfsimport() {
		String sourcePath = "C:\\temp\\test";
		String targetVOBdirectory = "M:\\tmpProj01_Dev_Dyn\\tmpVOB\\tmpComp01\\folder1\\datastageTest\\target";
		List<String> parameterList = Arrays.asList(ClearFsImportUtils.RECURSE,
				ClearFsImportUtils.NSETEVENT);
		ClearFsImportUtils.runClearfsimport(sourcePath, targetVOBdirectory,
				parameterList, "comment1");
	}
}
