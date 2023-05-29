package oz.utils.ldap;

import java.util.List;
import java.util.logging.Logger;

import javax.naming.ldap.LdapContext;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.ldap.LdapUtils;
import oz.infra.logging.jul.JulUtils;

public class LdapAttributes {
	// corp
	public final static String DOMAIN = "fibi.corp";
	public final static String DEFAULT_FIBI_BASE_ENTRY_DN = "DC=fibi,DC=corp";
	public final static String LDAP_SERVER = "fibi.corp";
	// exit
	public final static String DOMAIN_EXT = "fibi.ext";
	public final static String DEFAULT_FIBI_EXT_BASE_ENTRY_DN = "DC=fibi,DC=ext";

	// public final static String LDAP_SERVER_EXT = "192.168.26.116";
	// public final static String LDAP_SERVER_EXT = "192.168.26.135";
	public static String LDAP_SERVER_EXT = "fibi.ext";

	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String userName = args[0];
		String password = args[1];
		String domain = args[2];
		// String attrIDsString ="cn,sAMAccountName,memberOf";
		String attrIDsString = args[3];
		if (args.length > 4) {
			LDAP_SERVER_EXT = args[4];
		}
		StopWatch stopWatch = new StopWatch();
		List<List<String>> attributesList = getAttribute(userName, password, domain, attrIDsString);
		logger.info("**************************" + attributesList.get(0).get(0));
		stopWatch.logElapsedTimeMessage("All done");
	}

	public static List<List<String>> getAttribute(String user, String pass, String domain, final String attrIDsString) {
		logger.info("attributes: " + attrIDsString);
		// String filter = "(&(objectCategory=Person)(sAMAccountName=" + user +
		// "))";
		 String filter = "(&(objectCategory=Person))";
		// String filter = "(&(objectCategory=Person)(sAMAccountName=" + "*s1775*" + "))";
		String baseEntry = DEFAULT_FIBI_BASE_ENTRY_DN;
		String ldapServer = LDAP_SERVER;
		if (domain.equals(DOMAIN_EXT)) {
			ldapServer = LDAP_SERVER_EXT;
			baseEntry = DEFAULT_FIBI_EXT_BASE_ENTRY_DN;
		}
		LdapContext ldapCtx = LdapUtils.getLdapContext(user, pass, domain, ldapServer);
		logger.info(ldapCtx.toString());
		String[] attrIDs = attrIDsString.split(OzConstants.COMMA);
		StopWatch stopWatch = new StopWatch();
		List<List<String>> attributesList = LdapUtils.getAttributes(attrIDs, filter, ldapCtx, baseEntry);
		stopWatch.logElapsedTimeMessage("Return from getAttributes ");
		// if (attr_lst != null && !attr_lst.isEmpty()) {
		// Iterator<String> itr = attr_lst.iterator();
		// return itr.next();
		// } else
		// return null;
		return attributesList;
	}
}
