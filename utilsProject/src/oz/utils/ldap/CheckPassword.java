package oz.utils.ldap;

import java.util.logging.Logger;

import javax.naming.ldap.LdapContext;
import javax.swing.JOptionPane;

import oz.infra.datetime.StopWatch;
import oz.infra.ldap.LdapUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;
import oz.infra.swing.joptionpane.JOptionPaneUtils;

public class CheckPassword {
	private static final String FAILURE_PREFIX = "Bind has failed ";
	private static final String SUCCESS_PREFIX = "Bind has succeeded ";
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String userName = args[0];
		String password = args[1];
		String domain = args[2];
		String ldapServerHost = args[3];
		String message = "for user=" + userName + "@" + domain + " password=" + password + " \nLdap server host: "
				+ ldapServerHost;
		StopWatch stopWatch = new StopWatch();
		LdapContext ldapContext = LdapUtils.getLdapContext(userName, password, domain, ldapServerHost);
		String elapsedTimeString = stopWatch.getElapsedTimeString();
		String prefix = FAILURE_PREFIX;
		if (ldapContext != null) {
			prefix = SUCCESS_PREFIX;
		}
		message = StringUtils.concat("\n", prefix, message, "\n", elapsedTimeString);
		logger.info(message);
		JOptionPaneUtils.showMessageDialog(null, message, prefix, JOptionPane.ERROR_MESSAGE);
	}
}
