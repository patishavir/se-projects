package oz.clearcase.utils;

import oz.clearcase.infra.ClearCaseUtils;

public class createRmelemBatFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String folderPath2Clean = null;
		// folderPath2Clean =
		// "M:\\tmpProj01_Dev_Dyn\\tmpVOB\\tmpComp01\\folder1\\datastageTest\\target";
		// folderPath2Clean =
		// "M:\\dataStage_Dev_Dyn\\dataStageVOB\\dataStageComp\\deployments\\2014\\T";
		// folderPath2Clean =
		// "M:\\dataStage_Dev_Dyn\\dataStageVOB\\dataStageComp\\deployments\\2014\\Q";
		// folderPath2Clean = "M:\\Portal_Dev_Dyn\\intVOB\\lost+found";
		// folderPath2Clean =
		// "M:\\dataStage_Dev_Dyn\\dataStageVOB\\dataStageComp\\deployments\\scripts";
		// folderPath2Clean =
		// "M:\\tmpProj01_Dev_Dyn\\tmpVOB\\tmpComp01\\datastage\\datastage";
		// folderPath2Clean =
		// "M:\\tmpProj01_Dev_Dyn\\tmpVOB\\tmpComp01\\folder1\\datastageTest\\target\\T";
		// folderPath2Clean =
		// "M:\\dataStage_Dev_Dyn\\dataStageVOB\\dataStageComp\\deployments\\scripts";
		folderPath2Clean = "M:\\Snifit_BTT820_Next_Dev_Dyn\\App2\\PikdonotNewClient";
		// String folderPath2Clean =
		// folderPath2Clean = "M:\\dataStage_Dev_Dyn\\dataStageVOB\\lost+found";
		// folderPath2Clean =
		// "M:\\dataStage_Dev_Dyn\\dataStageVOB\\dataStageComp\\deployments";
		ClearCaseUtils.createRmelemBatFile(folderPath2Clean, "c:\\temp\\rmelem.bat");
	}
}
