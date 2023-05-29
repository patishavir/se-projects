package oz.clearcase.utils;

import oz.clearcase.infra.ClearCaseUtils;

public class createRmactivityBatFile {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String viewFolder = null;
		// viewFolder =
		// "M:\\tmpProj01_Dev_Dyn\\tmpVOB\\tmpComp01\\folder1\\datastageTest\\target";
		viewFolder = "M:\\dataStage_Dev_Dyn\\dataStageVOB\\dataStageComp\\deployments";
		viewFolder = "M:\\dataStage_Dev_Dyn\\dataStageVOB\\dataStageComp\\";
		String pvob = "dataStagePVOB";
		ClearCaseUtils.createRmactivityBatFile(viewFolder, pvob, "c:\\temp\\Rmactivity.bat");
	}
}
