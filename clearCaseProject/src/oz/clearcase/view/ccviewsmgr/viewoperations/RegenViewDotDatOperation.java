package oz.clearcase.view.ccviewsmgr.viewoperations;

import java.io.File;

import javax.swing.JOptionPane;

import oz.clearcase.view.ccviewsmgr.CCViewsParameters;
import oz.infra.run.RunExec;

public class RegenViewDotDatOperation implements IViewOperation {
	public boolean exec(EViewOperation viewOperation, String[] params) {
		boolean returnCode = false;
		// usage: C:/atria/bin/ccperl regen_view_dot_dat.pl [-tag
		// snapshot-view-tag] snapshot-view-pname
		String param = params[viewOperation.getParamsIndex()];
		int i = param.indexOf("_");
		String tagString = param.substring(i + 1, param.length());
		String pname = "c:\\CCViews\\" + tagString;
		String regenViewDotDatPath = CCViewsParameters.getRegenViewDotDatPath();
		String ccperlPath = CCViewsParameters.getCcperlPath();
		File ccPerlFile = new File(ccperlPath);
		if (!ccPerlFile.exists()) {
			ccperlPath = (String) JOptionPane.showInputDialog(null, "Enter ccperl.exe full path:",
					"Input", JOptionPane.INFORMATION_MESSAGE, null, null, ccperlPath);
		}
		File regen_view_dot_dat_File = new File(regenViewDotDatPath);
		if (!regen_view_dot_dat_File.exists()) {
			regenViewDotDatPath = (String) JOptionPane.showInputDialog(null,
					"Enter regen_view_dot_dat.pl full path:", "Input",
					JOptionPane.INFORMATION_MESSAGE, null, null, regenViewDotDatPath);
		}
		File pnameFile = new File(pname);
		if (!pnameFile.exists()) {
			pnameFile.mkdirs();
		}
		if (pnameFile.isDirectory()) {
			RunExec re = new RunExec();
			String[] reParams = { ccperlPath, regenViewDotDatPath, "-tag", param, pname };
			int retCode = re.runCommand(reParams);

			returnCode = (retCode == 0);

			if (re.getErrString().length() > 0) {
				JOptionPane.showMessageDialog(null, re.getErrString(), "Error message",
						JOptionPane.ERROR_MESSAGE, null);
			}
			if (re.getOutputString().length() > 0) {
				JOptionPane.showMessageDialog(null, re.getOutputString(), "Output message",
						JOptionPane.INFORMATION_MESSAGE, null);
			}
			if (returnCode) {
				String[] ccUpdateParams = { null, "update ", "-ptime", pname };
				MultipleViewsOperation.add2Script(ccUpdateParams);
			}

			return returnCode;
		}
		/*
		 * pname is not a directory
		 */
		JOptionPane.showMessageDialog(null, pname + " must be a directory!", "Error message",
				JOptionPane.ERROR_MESSAGE, null);
		return returnCode;
	}
}