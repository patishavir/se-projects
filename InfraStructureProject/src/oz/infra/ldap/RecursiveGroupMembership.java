package oz.infra.ldap;

import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import oz.infra.datetime.StopWatch;
import oz.infra.logging.jul.JulUtils;
import oz.infra.system.SystemUtils;

public class RecursiveGroupMembership {

	private static final String[] DEFULT_RETURNED_ATTS = { "sAMAccountName", "employeeID", "mail", "displayName",
			"telephoneNumber", "extensionAttribute9" };
	private static final String[] MEMBER_OF = { "memberOf" };

	private static SearchControls searchCtls = null;
	private static javax.naming.ldap.LdapContext ldapCtx = null;

	private static Vector<String> userGroups = new Vector<String>(10);

	// private Document doc = null;
	private static Vector<String> gr = new Vector<String>();

	private static String ldapUrl = "";
	private static String domain = "";
	private static String baseEntry = "";
	private static String bindUser = "";
	private static String bindPassword = "";
	private static String user = "";

	private static StringBuilder sb = new StringBuilder(SystemUtils.LINE_SEPARATOR);
	private static Logger logger = JulUtils.getLogger();

	private static void login(final String adUsr, final String adPwd, final String user) throws Exception {

		Hashtable<String, Object> env = new Hashtable<String, Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "Simple");
		String securityPrincipal = adUsr + "@" + RecursiveGroupMembership.domain;
		env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
		env.put(Context.SECURITY_CREDENTIALS, adPwd);
		env.put(Context.PROVIDER_URL, ldapUrl); // ldap://ldapServer:389
		env.put(Context.REFERRAL, "follow");
		// env.put("com.sun.jndi.ldap.trace.ber", System.err);
		ldapCtx = new InitialLdapContext(env, null);

		searchCtls = new SearchControls();
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		searchCtls.setReturningAttributes(DEFULT_RETURNED_ATTS);
		String searchFilter = "(sAMAccountName=" + user + ")";
		NamingEnumeration answer = ldapCtx.search(RecursiveGroupMembership.baseEntry, searchFilter, searchCtls);

		String attrName = null;
		String attrValue = null;

		while (answer.hasMoreElements()) {
			SearchResult sr = (SearchResult) answer.next();
			Attributes attrs = sr.getAttributes();
			if (attrs != null) {
				for (NamingEnumeration all = attrs.getIDs(); all.hasMoreElements();) {
					attrName = (String) all.next();
					Attribute attr = attrs.get(attrName);
					for (int j = 0; j < attr.size(); j++) {
						Object obj = attr.get(j);
						attrValue = obj.toString();
						sb.append(attrName + ": " + attrValue + SystemUtils.LINE_SEPARATOR);
					}
				}
			}
		}

		memberOf(user, RecursiveGroupMembership.baseEntry);

		// set userGroups
		int l = userGroups.size();
		String[] allGroups = new String[l];
		StringBuilder sb = new StringBuilder(); // debug
		for (int i = 0; i < l; i++) {
			allGroups[i] = userGroups.get(i);
			sb.append("\n" + allGroups[i]);// debug
		}
		return;
	}

	public static void main(final String[] args) {
		if (args.length < 6) {
			logger.warning(
					"use java Ldap <ldapUrl[ldap://ldapServer:389]> <domain> <base entry> <bindUser> <bindPassword> <user>");
			System.exit(1);

		}
		ldapUrl = args[0];
		domain = args[1];
		baseEntry = args[2];
		bindUser = args[3];
		bindPassword = args[4];
		user = args[5];

		try {
			StopWatch stopWatach = new StopWatch();
			RecursiveGroupMembership ldap = new RecursiveGroupMembership();
			RecursiveGroupMembership.login(bindUser, bindPassword, user);
			int count = 0;

			for (String userGroup1 : ldap.userGroups) {
				sb.append(String.valueOf(++count) + " " + userGroup1 + SystemUtils.LINE_SEPARATOR);
			}
			sb.append("\nurl: " + ldapUrl + " elapsed time:" + stopWatach.getElapsedTimeString());
			logger.info(sb.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void memberOf(final String user, final String baseEntry) throws Exception {
		if (gr.contains(user))
			return;
		gr.add(user);
		String searchFilter = "(sAMAccountName=" + user + ")";
		String key = null;
		String value = null;
		searchCtls.setReturningAttributes(MEMBER_OF);
		for (NamingEnumeration answer = ldapCtx.search(baseEntry, searchFilter, searchCtls); answer
				.hasMoreElements();) {
			SearchResult sr = (SearchResult) answer.next();
			Attributes attrs = sr.getAttributes();
			if (attrs != null) {
				for (NamingEnumeration all = attrs.getIDs(); all.hasMoreElements();) {
					key = (String) all.next();
					Attribute attr = attrs.get(key);
					for (int j = 0; j < attr.size(); j++) {
						try {
							value = (String) attr.get(j);
							value = value.substring(3, value.indexOf(','));
							if (!userGroups.contains(value)) {
								// call the query again to get group of group
								userGroups.add(value);
							}
							memberOf(value, baseEntry);
						} catch (Exception ex) {

						}
					}
				}
			}
		}
	}

}
