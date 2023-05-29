package oz.utils.ssh;

import oz.infra.ssh.SshUtils;

public class Ssh {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// "Usage: SCPExample <server:port> <username> <password> to|from <src_file> <dst_file>"
		SshUtils.runRemoteCommand(args[0]);
	}
}
