package oz.utils.ssh.scp;

import oz.infra.ssh.scp.ScpUtils;

public class Scp {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		// "Usage: SCPExample <server:port> <username> <password> to|from <src_file> <dst_file>"
		ScpUtils.scp(args[0]);
	}
}
