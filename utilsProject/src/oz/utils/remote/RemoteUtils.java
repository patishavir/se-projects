package oz.utils.remote;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

import oz.infra.io.filefilter.FilefilterIsFile;
import oz.infra.logging.jul.JulUtils;
import oz.infra.ssh.jsch.JschUtils;
import oz.infra.system.SystemUtils;

public class RemoteUtils {
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		logger.info(SystemUtils.getRunInfo());
		runRemoteCommands(args[0]);
	}

	public static void runRemoteCommands(final String folderPath) {
		File folderFile = new File(folderPath);
		File[] files = folderFile.listFiles(new FilefilterIsFile());
		String[] filePaths = new String[files.length];
		for (int i = 0; i < files.length; i++) {
			filePaths[i] = files[i].getAbsolutePath();
		}
		Arrays.sort(filePaths);
		for (int i = 0; i < filePaths.length; i++) {
			JschUtils.runOperation(filePaths[i]);
		}
		logger.info(String.valueOf(filePaths.length).concat(" operations have been performed !"));
	}
}