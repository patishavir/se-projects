package oz.utils.ldap;

import java.security.Security;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;

public class ADConnection {

	private DirContext ldapContext;
	private String baseName = ",cn=users,DC=fabrikam,DC=com";
	private String serverIP = "10.1.1.7";

	public ADConnection() {
		try {
			Hashtable ldapEnv = new Hashtable(11);
			ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			ldapEnv.put(Context.PROVIDER_URL, "ldap://" + serverIP + ":636");
			ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			ldapEnv.put(Context.SECURITY_PRINCIPAL, "cn=administrator" + baseName);
			ldapEnv.put(Context.SECURITY_CREDENTIALS, "PA$$w0rd");
			ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
			ldapContext = new InitialDirContext(ldapEnv);
		} catch (Exception ex) {
			System.out.println(" bind error: " + ex);
			ex.printStackTrace();
			System.exit(-1);
		}
	}

	public void updatePassword(final String username, final String password) {
		try {
			String quotedPassword = "\"" + password + "\"";
			char unicodePwd[] = quotedPassword.toCharArray();
			byte pwdArray[] = new byte[unicodePwd.length * 2];
			for (int i = 0; i < unicodePwd.length; i++) {
				pwdArray[i * 2 + 1] = (byte) (unicodePwd[i] >>> 8);
				pwdArray[i * 2 + 0] = (byte) (unicodePwd[i] & 0xff);
			}
			ModificationItem[] mods = new ModificationItem[1];
			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(
					"UnicodePwd", pwdArray));
			ldapContext.modifyAttributes("cn=" + username + baseName, mods);
		} catch (Exception e) {
			System.out.println("update password error: " + e);
			System.exit(-1);
		}
	}

	public static void main(final String[] args) {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		// the keystore that holds trusted root certificates
		System.setProperty("javax.net.ssl.trustStore", "c:\\myCaCerts.jks");
		System.setProperty("javax.net.debug", "all");
		ADConnection adc = new ADConnection();
		adc.updatePassword("Java User2", "pass@word3");
	}
}
