package oz.infra.ldap.test;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.ldap.LdapContext;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.datetime.StopWatch;
import oz.infra.ldap.LdapHelperUtils;
import oz.infra.ldap.LdapUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.KeyValuePair;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;
import oz.infra.thread.ThreadUtils;

public class TestLdapUtils {
	private static LdapContext ldapCtx = LdapUtils.getLdapContext("s177571", "ujik99");
	private static Logger logger = JulUtils.getLogger();

	private static void getRecursiveGroupMembershipByTokenGroup() {
		Properties properties = new Properties();
		properties.setProperty("bindUser", "s177571@fibi.corp");
		properties.setProperty("bindPassword", "ujik99");
		properties.setProperty("ldapURL", "ldap://fibi.corp:389");
		properties.setProperty("userSearchBase", "DC=fibi,DC=corp");
		properties.setProperty("groupsSearchBase", "DC=fibi,DC=corp");
		StringBuilder sb = LdapHelperUtils.getRecursiveGroupMembershipByTokenGroup(properties);
		logger.info(sb.toString());
	}

	private static void getSmtpAddressesByNames() {
		// String[] namesArray = FileUtils
		// .readTextFile2Array(".\\src\\oz\\infra\\ldap\\test\\distributionlist.txt");
		String[] namesArray = { "564 מד' תוכנה סניפית", "565 מד' ישומי ניע בתקשורת" };
		ArrayUtils.printArray(namesArray);
		String[] attributesArray = ArrayUtils.populateArray("displayName", namesArray.length);
		ArrayUtils.printArray(LdapHelperUtils.getSmtpAddresses(attributesArray, namesArray, ldapCtx),
				SystemUtils.LINE_SEPARATOR, null);
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// testGetSecuredLdapContext();
		// String[] attibutes2Get = { "mobile" };
		// KeyValuePair<String, String> filter = new KeyValuePair<String,
		// String>("sAMAccountName", "s177571");
		// testGetAttributes(attibutes2Get, filter);
		// KeyValuePair<String, String> filter = new KeyValuePair<String,
		// String>("mailNickname", "MATAF PROGRAM B4");
		// KeyValuePair<String, String> filter = new KeyValuePair<String,
		// String>("mailNickname",
		// "MATAFPROGRAMB5");
		// KeyValuePair<String, String> filter = new KeyValuePair<String,
		// String>("mailNickname", "MATAF PROGRAM B8");
		// LdapHelperUtils.getGroupMemnersAttributes("s177571", "nnui78",
		// filter, ActiveDirectoryUtils.SAM_ACCOUNT_NAME);

		// testGetAttributes();
		// testGetSmtpAddressByName();
		// getSmtpAddressesByNames();
		// testGetAttributes0();

		// testGetAttributes();
		// testGetAttributes3();
		// testGetAttributes4();
		// testGetGroupMembers();
		// testGetGroupMemberNames();
		// testGetMyGroups();
		// testGetLdapContext(args);
		 getRecursiveGroupMembershipByTokenGroup();
		// testGetSmtpAddressByName();
		// testGetLdapContext(null);
		// testGetSecuredLdapContext();
	}

	private static void testGetAttributes() {
		// String[] attrIDs = { "distinguishedName", "proxyAddresses" };
		StopWatch stopWatch = new StopWatch();
		String[] attrIDs = { "mobile" };
		String sAMAccountName = "s177571";
		List<List<String>> attributesList = LdapUtils.getAttributes(attrIDs, "sAMAccountName=", sAMAccountName,
				ldapCtx);
		ListUtils.getAsDelimitedString(attributesList, "\nAttributes for sAMAccountName: " + sAMAccountName + "\n",
				Level.INFO);
		stopWatch.getElapsedTimeLong();
		// LdapUtils.getAttributes(attrIDs, "name=", name, ldapCtx);
	}

	private static List<List<String>> testGetAttributes(final String[] attibutes2Get,
			final KeyValuePair<String, String> filter) {
		StopWatch stopWatch = new StopWatch();
		List<List<String>> attributesList = LdapUtils.getAttributes(attibutes2Get,
				filter.getKey().concat(OzConstants.EQUAL_SIGN), filter.getValue(), ldapCtx);
		ListUtils.getAsDelimitedString(attributesList,
				StringUtils.concat("\nAttributes for ", filter.getKey(), ": ", filter.getValue(), "\n"), Level.INFO);
		stopWatch.getElapsedTimeLong();
		return attributesList;
	}

	private static void testGetAttributes0() {
		// String[] attrIDs = { "distinguishedName", "proxyAddresses" };
		StopWatch stopWatch = new StopWatch();
		String[] attrIDs = { "member" };
		String mailNickname = "MATAF PROGRAM B4";
		List<List<String>> attributesList = LdapUtils.getAttributes(attrIDs, "mailNickname=", mailNickname, ldapCtx);
		ListUtils.getAsDelimitedString(attributesList, "\nAttributes for mailNickname: " + mailNickname + "\n",
				Level.INFO);
		stopWatch.getElapsedTimeLong();
		// LdapUtils.getAttributes(attrIDs, "name=", name, ldapCtx);
	}

	private static void testGetAttributes1() {
		String[] attrIDs = { "member" };
		String name = "ClearCaseAd*";
		List<List<String>> attributesList = LdapUtils.getAttributes(attrIDs, "name=", name, ldapCtx);
		ListUtils.printListOfLists(attributesList, " Member list:\n", Level.INFO, "\n");
		LdapUtils.getAttributes(attrIDs, "name=", name, ldapCtx);
	}

	// private static void testGetAttributes2() {
	// String[] attrIDs = { "name", "objectClass" };
	// String name = "ClearCase*";
	// List<List<String>> attributesList = LdapUtils.getAttributes(attrIDs,
	// "name=", name, ldapCtx);
	// ArrayUtils.printArray(attributes, "\n", name + " Member list:\n");
	// }

	// private static void testGetAttributes3() {
	// StopWatch stopWatch = new StopWatch();
	// String[] attrIDs = { "mobile", "name", "sAMAccountName", "proxyAddresses"
	// };
	// String filter = "(&(objectCategory=person)(objectClass=user)(mobile=*))";
	// List<List<String>> attributesList = LdapUtils.getAttributes(attrIDs,
	// filter, ldapCtx);
	// ArrayUtils.printArray(attributes, "\n", filter + " list:\n");
	// logger.info("Number of entries: " + String.valueOf(attributes.length));
	// stopWatch.getElapsedTime();
	// }

	// private static void testGetAttributes4() {
	// String[] attrIDs = { "name", "userAccountControl" };
	// String sAMAccountName = "s177571";
	// List<List<String>> attributesList = LdapUtils
	// .getAttributes(attrIDs, "sAMAccountName=", sAMAccountName, ldapCtx);
	// ArrayUtils.printArray(attributes, "\n", sAMAccountName + " Member
	// list:\n");
	// }

	// private static void testGetGroupMemberNames() {
	// StopWatch stopWatch = new StopWatch();
	// String[] attrIDs = { "member" };
	// String filter =
	// "(&(objectCategory=Group)(objectClass=group)(name=ClearCaseAdmins))";
	// List<List<String>> attributesList = LdapUtils.getAttributes(attrIDs,
	// filter, ldapCtx);
	// logger.info(attributes[0]);
	// String[] membersArray = attributes[0].split(OzConstants.SEMICOLON);
	// ArrayUtils.printArray(membersArray, "\n", filter + " list:\n");
	// logger.info("Number of members: " + String.valueOf(membersArray.length));
	// String[] nameAttr = { "sAMAccountName" };
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < membersArray.length; i++) {
	// String memberFilter = "(&(objectClass=user)(distinguishedName=" +
	// membersArray[i] + "))";
	// logger.finest("memberFilter: " + memberFilter);
	// List<List<String>> attributesList = LdapUtils.getAttributes(nameAttr,
	// memberFilter, ldapCtx);
	// logger.finest(memberAttributes[0]);
	// sb.append(OzConstants.LINE_FEED);
	// sb.append(memberAttributes[0]);
	// }
	// logger.info(sb.toString());
	// stopWatch.getElapsedTime();
	// }

	// private static void testGetGroupMembers() {
	// StopWatch stopWatch = new StopWatch();
	// String[] attrIDs = { "member" };
	// String filter =
	// "(&(objectCategory=Group)(objectClass=group)(name=ClearCaseAdmins))";
	// List<List<String>> attributesList = LdapUtils.getAttributes(attrIDs,
	// filter, ldapCtx);
	// String[] membersArray = attributes[0].split("CN=");
	// ArrayUtils.printArray(membersArray, "\n", filter + " list:\n");
	// logger.info("Number of members: " + String.valueOf(membersArray.length));
	// stopWatch.getElapsedTime();
	// }

	private static void testGetLdapContext(final String[] args) {
		if (args == null || args.length < 4) {
			// testGetLdapContext1("ADWASSnifProdMgrSPN", "Wzo3Mp4F");
			// testGetLdapContext1("ADWASSnifTestMgrSPN", "T68yXEvm");
			// testGetLdapContext1("s177571", "version7");
			// testGetLdapContext1("ADWebSphereBindT", "z13579");
			// testGetLdapContext1("YASHIR_TEST", "Z13579", "fibi.ext");
			// testGetLdapContext1("s177571", "version7", "fibi.corp",
			// "10.18.188.135");
			// testGetLdapContext1("y180564", "qet1357", "fibi.ext",
			// "192.168.26.116");
			// testGetLdapContext1("T180564", "qet1357", "fibi.corp",
			// "fibi.corp");
			testGetLdapContext1("ADWAS6Adm", "qrt531ef");
		} else {
			testGetLdapContext1(args[0], args[1], args[2], args[3]);
		}
	}

	private static void testGetLdapContext1(final String userName, final String password, String... parameters) {
		LdapContext testLdapCtx = null;
		if (parameters.length == 0) {
			testLdapCtx = LdapUtils.getLdapContext(userName, password);
		} else {
			testLdapCtx = LdapUtils.getLdapContext(userName, password, parameters[0], parameters[1]);
		}
		if (testLdapCtx == null) {
			logger.info("bind failed for " + userName);
		} else {
			logger.info(testLdapCtx.toString() + " bind succeeded for " + userName);
		}
	}

	// private static void testGetMyGroups() {
	// StopWatch stopWatch = new StopWatch();
	// String[] attrIDs = { "displayName", "memberOf" };
	// String filter = "(&(objectCategory=Person)(sAMAccountName=s177571))";
	// List<List<String>> attributesList = LdapUtils.getAttributes(attrIDs,
	// filter, ldapCtx, "DC=fibi,DC=corp");
	// logger.info(attributes[0]);
	// String[] membersArray = attributes[0].split(OzConstants.SEMICOLON);
	// ArrayUtils.printArray(membersArray, "\n", filter + " list:\n");
	// logger.info("Number of members: " + String.valueOf(membersArray.length));
	// String[] nameAttr = { "sAMAccountName" };
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < membersArray.length; i++) {
	// String memberFilter = "(&(objectClass=user)(distinguishedName=" +
	// membersArray[i] + "))";
	// logger.finest("memberFilter: " + memberFilter);
	// List<List<String>> attributesList = LdapUtils.getAttributes(nameAttr,
	// memberFilter, ldapCtx);
	// logger.finest(memberAttributes[0]);
	// sb.append(OzConstants.LINE_FEED);
	// sb.append(memberAttributes[0]);
	// }
	// logger.info(sb.toString());
	// stopWatch.getElapsedTime();
	// }

	private static void testGetSecuredLdapContext() {
		String userName = "s177571";
		String password = "ujik99";
		String domain = "fibi.corp";
		String ldapServer = "fibi.corp";
		LdapContext ldapContext = LdapUtils.getSecureLdapContext(userName, password, domain, ldapServer,
				"c:\\oj\\security\\trust.jks", "WebAS");
		System.out.println(ldapContext.toString());
		ThreadUtils.sleep(1000000l, Level.INFO);
		logger.info(ldapContext.toString());
	}

	private static void testGetSmtpAddressByName() {
		String name = "netsecurity";
		logger.info("=" + LdapHelperUtils.getSmtpAddress("name=", name, ldapCtx) + "=");
		String[] attributesArray = { "name=", "name=" };
		String[] namesArray = { "netsecurity" };
		ArrayUtils.printArray(LdapHelperUtils.getSmtpAddresses(attributesArray, namesArray, ldapCtx), Level.INFO);
	}
}