package oz.utils.ldap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.naming.ldap.LdapContext;

import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.io.FileUtils;
import oz.infra.ldap.LdapUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.string.StringUtils;

public class AdCnDuplicates {
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
	private static Map<String, String> cnSamAccountNameMap = new HashMap<String, String>();
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
		findCnDuplicates(attributesList);
		stopWatch.logElapsedTimeMessage("All done");
	}

	private static List<List<String>> getAttribute(String user, String pass, String domain, final String attrIDsString) {
		logger.info("attributes: " + attrIDsString);
		// String filter = "(&(objectCategory=Person)(sAMAccountName=" + user +
		// "))";
		String filter = "(&(objectCategory=Person))";
		// String filter = "(&(objectCategory=Person)(sAMAccountName=" +
		// "*s1775*" + "))";
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

	private static void findCnDuplicates(final List<List<String>> attributesList) {
		int dupCount = 0;

		for (List<String> list1 : attributesList) {
			String samAcountName = list1.get(0);
			if (list1.size() > 1) {
				String cn = list1.get(1);
				logger.finest(cn);
				if (cnSamAccountNameMap.get(cn) == null) {
					cnSamAccountNameMap.put(cn, samAcountName);
				} else {
					String currentSamAcountName = cnSamAccountNameMap.get(cn);
					cnSamAccountNameMap.put(cn,
							StringUtils.concat(currentSamAcountName, OzConstants.COMMA, samAcountName));
					// sb.append(StringUtils.concat("duplicate cn. cn:", cn,
					// " samAcountName: ", samAcountName, " , ",
					// cnMap.get(cn), "\n"));
					dupCount++;
				}
			} else {
				logger.info(samAcountName + "\t==================== no cn !!!!!!!!!!!!!!!!!!");
			}
		}
		int dupCount1 = 0;
		StringBuilder sb = new StringBuilder("\n");
		Set<Entry<String, String>> entrySet = cnSamAccountNameMap.entrySet();
		for (Entry<String, String> entry1 : entrySet) {
			if (entry1.getValue().indexOf(OzConstants.COMMA) > OzConstants.STRING_NOT_FOUND) {
				sb.append(StringUtils.concat(entry1.getKey(), OzConstants.EQUAL_SIGN, entry1.getValue(),
						OzConstants.LINE_FEED));
				dupCount1++;
			}
		}
		logger.info(sb.toString());
		logger.info(String.valueOf(dupCount).concat(" found ! ") + String.valueOf(dupCount1).concat(" found1 !"));
		logger.info(String.valueOf(cnSamAccountNameMap.size()).concat(" users in AD !"));
		readGroupMembersFile();
	}

	private static void readGroupMembersFile() {
		StringBuilder sb1 = new StringBuilder("\n");
		final String cnPrefix = "CN=";
		String textFilePath = "C:\\oj\\projects\\se\\utilsProject\\args\\ldap\\AdCnDuplicates\\284members.txt";
		String[] users = FileUtils.readTextFile2Array(textFilePath);
		for (int i = 0; i < users.length; i++) {
			String[] lineBreakDown = users[i].split(OzConstants.COMMA);
			logger.finest(lineBreakDown[0]);
			String cn = lineBreakDown[0].substring(cnPrefix.length());
			logger.finest(cn);
			String samAccountName = cnSamAccountNameMap.get(cn);
			if (samAccountName != null && samAccountName.indexOf(OzConstants.COMMA) > OzConstants.STRING_NOT_FOUND) {
				sb1.append(samAccountName + " " + cn + "\n");
			}
		}
		if (sb1.length() > 1) {
			logger.info(sb1.toString());
		} else {
			logger.info("cns are unique to users found in file " + textFilePath);
		}
	}
}
