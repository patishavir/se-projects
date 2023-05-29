package oz.temp.ldap;

import java.util.logging.Level;

import javax.naming.ldap.LdapContext;

import oz.infra.ldap.LdapUtils;
import oz.infra.thread.ThreadUtils;

public class LdapUtilsTest {

	private static void getldapsContect() {
		String userName = "s177571";
		String password = "ujik78";
		String domain = "fibi.corp";
		String ldapServer = "fibi.corp";
		LdapContext ldapContext = LdapUtils.getSecureLdapContext(userName, password, domain, ldapServer,
				"c:\\oj\\security\\trust.jks", "WebAS");
		System.out.println(ldapContext.toString());
		ThreadUtils.sleep(1000000l, Level.INFO);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getldapsContect();
	}
}