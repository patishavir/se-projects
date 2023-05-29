package oz.jsch.samples;

import java.io.File;
import java.io.FileInputStream;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;

import oz.infra.logging.jul.JulUtils;

public class SftpMain {
	private static java.util.logging.Logger logger = JulUtils.getLogger();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		sftp();
	}

	private static void sftp() {
		try {
			JSch jsch = new JSch();
			Session session = null;
			session = jsch.getSession("root", "suswastest2", 22);
			// session.setPassword("root123");
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			ChannelSftp channel = null;
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			File localFile = new File("c:\\temp\\1.txt");
			// If you want you can change the directory using the following
			// line.
			channel.cd("/tmp");
			channel.put(new FileInputStream(localFile), localFile.getName());
			logger.info( "file copied.");
			channel.disconnect();
			session.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
