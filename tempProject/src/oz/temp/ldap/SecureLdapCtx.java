package oz.temp.ldap;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import oz.infra.map.MapUtils;
import oz.infra.system.SystemPropertiesUtils;

public class SecureLdapCtx {
	private static final String LDAPS_DEFAULT_PROTOCOL = "ldaps";
	private static final String LDAPS_DEFAULT_PORT = "636";
	private static Logger logger = Logger.getLogger(SecureLdapCtx.class.getName());

	private static Hashtable<String, String> getEnvHashTable(final String userName, final String password,
			final String domain, final String ldapServer, final String protocol, final String port) {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "Simple");
		String securityPrincipal = userName + "@" + domain;
		env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.PROVIDER_URL, protocol + "://" + ldapServer + ":" + port);
		env.put(Context.REFERRAL, "follow");
		return env;
	}

	public static LdapContext getLdapContext(Hashtable<String, String> env) {
		LdapContext myLdapCtx = null;
		try {
			MapUtils.printMap(env, "JNDI env", Level.FINEST);
			myLdapCtx = new InitialLdapContext(env, null);
			logger.finest("Connection Successful.");
		} catch (NamingException nex) {
			logger.warning("LDAP Connection: FAILED");
			logger.warning(nex.getMessage());
			nex.printStackTrace();
		}
		return myLdapCtx;
	}

	public static LdapContext getSecureLdapContext(final String userName, final String password, final String domain,
			final String ldapServer, final String trustStoreFilePath, final String trustStorePassword) {
		System.setProperty(SystemPropertiesUtils.SYSTEM_PROPERTY_TRUST_STORE, trustStoreFilePath);
		System.setProperty(SystemPropertiesUtils.SYSTEM_PROPERTY_TRUST_STORE_PASSWORD, trustStorePassword);
		String protocol = LDAPS_DEFAULT_PROTOCOL;
		String port = LDAPS_DEFAULT_PORT;
		Hashtable<String, String> env = getEnvHashTable(userName, password, domain, ldapServer, protocol, port);
		env.put(Context.SECURITY_PROTOCOL, "tls");
		return getLdapContext(env);
	}

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		LdapContext ldapContext = getSecureLdapContext(args[0], args[1], args[2], args[3], args[4], args[5]);
		logger.info(ldapContext.toString());
	}

}
