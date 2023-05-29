package oz.temp.ldap;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

public class Ldap {
	private Vector<String> gr = new Vector<String>();
	private static final String DEFULT_RETURNED_ATTS[] = { "sAMAccountName",
			"employeeID", "mail", "displayName", "telephoneNumber",
			"extensionAttribute9" };
	private static final String MEMBER_OF[] = { "memberOf" };

	private SearchControls searchCtls = null;
	private javax.naming.ldap.LdapContext ldapCtx = null;

	String baseEntry = "DC=fibi,DC=ext";
	String domain = "fibi.ext";
	String server = "192.168.26.135";

	/**
	 *  
	 */
	public static void main(String[] argv) {
		if (argv.length < 4) {
			System.out.println("call app with ext/int , adUsr, adPwd,  user");
			System.exit(0);

		}
		try {
			Ldap ldap = null;
			if (argv[0].equalsIgnoreCase("int")) {
				ldap = new Ldap("DC=fibi,DC=corp", "fibi.corp","10.18.188.135");
			} else {
				ldap = new Ldap("DC=fibi,DC=ext", "fibi.ext", "192.168.26.135");
			}
			ldap.userInfo(argv[1], argv[2], argv[3]);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Ldap(String baseEntry, String domain, String server) {
		this.baseEntry = baseEntry;
		this.domain = domain;
		this.server = server;
	}

	public void userInfo(String adUsr, String adPwd, String user)
			throws Exception {

		Hashtable<String, String> env = new Hashtable<String, String>();

		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "Simple");
		env.put(Context.SECURITY_PRINCIPAL, adUsr + "@" + domain);
		env.put(Context.SECURITY_CREDENTIALS, adPwd);
		env.put(Context.PROVIDER_URL, "ldap://" + server + ":389");
		env.put(Context.REFERRAL, "follow");
		ldapCtx = new InitialLdapContext(env, null);

		searchCtls = new SearchControls();
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		searchCtls.setReturningAttributes(DEFULT_RETURNED_ATTS);
		String searchFilter = "(sAMAccountName=" + user + ")";
		NamingEnumeration answer = ldapCtx.search(baseEntry, searchFilter,
				searchCtls);
		String attrValue = "";
		String attrName = "";
		while (answer.hasMoreElements()) {
			SearchResult sr = (SearchResult) answer.next();
			Attributes attrs = sr.getAttributes();
			if (attrs != null) {
				for (NamingEnumeration all = attrs.getIDs(); all
						.hasMoreElements();) {
					attrName = (String) all.next();
					Attribute attr = attrs.get(attrName);
					for (int j = 0; j < attr.size(); j++) {
						Object obj = attr.get(j);
						attrValue = obj.toString();
					}
					System.out.println(attrName + ": " + attrValue);
				}

			}
		}
	}

	private void memberOf(String user, String baseEntry) throws Exception {
		if (gr.contains(user))
			return;
		gr.add(user);

		String searchFilter = "(sAMAccountName=" + user + ")";
		String key = null;
		String value = null;

		searchCtls.setReturningAttributes(MEMBER_OF);
		for (NamingEnumeration answer = ldapCtx.search(baseEntry, searchFilter,
				searchCtls); answer.hasMoreElements();) {
			SearchResult sr = (SearchResult) answer.next();
			Attributes attrs = sr.getAttributes();
			if (attrs != null) {
				for (NamingEnumeration all = attrs.getIDs(); all
						.hasMoreElements();) {
					key = (String) all.next();
					Attribute attr = attrs.get(key);
					for (int j = 0; j < attr.size(); j++) {
						try {
							value = (String) attr.get(j);
							value = value.substring(3, value.indexOf(','));
							memberOf(value, baseEntry);
						} catch (Exception ex) {

						}
					}
				}
			}
		}
	}

}
