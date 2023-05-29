package oz.infra.ldap;

import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import oz.infra.array.ArrayUtils;
import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.list.ListUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.KeyValuePair;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemUtils;

public class LdapHelperUtils {
	private static Logger logger = JulUtils.getLogger();

	public static String get1stSmtpAddressFromProxyAddresses(final String proxyAddresses) {
		final String primarySmtpAddressPrefix = "SMTP:";
		String smtpAddress = null;
		if (proxyAddresses != null) {
			String[] proxyAddressesArray = proxyAddresses.split(OzConstants.SEMICOLON);
			for (String proxAddress : proxyAddressesArray) {
				if (proxAddress.startsWith(primarySmtpAddressPrefix)) {
					smtpAddress = proxAddress.substring(primarySmtpAddressPrefix.length());
				}
			}
		}
		logger.info(smtpAddress);
		return smtpAddress;
	}

	public static String getDistributionList(final String[] attributeNames, final String attributeName,
			final String[] attributeValues, final String userName, final String password, final String delimiter) {
		LdapContext ldapCtx = LdapUtils.getLdapContext(userName, password);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < attributeValues.length; i++) {
			String filter = attributeName + attributeValues[i];
			List<List<String>> attributesList = LdapUtils.getAttributes(attributeNames, filter, ldapCtx);
			for (String str1 : attributesList.get(0)) {
				sb.append(str1);
			}
			sb.append(delimiter);
		}
		return sb.toString();
	}

	public static void getGroupMemnersAttributes(final String userName, final String password,
			final KeyValuePair<String, String> filter, final String requiredAttribute) {
		LdapContext ldapCtx = LdapUtils.getLdapContext(userName, password);
		String[] attibutes2Get = { "member" };
		List<List<String>> attributesList = LdapUtils.getAttributes(attibutes2Get,
				filter.getKey().concat(OzConstants.EQUAL_SIGN), filter.getValue(), ldapCtx);
		String[] dns = attributesList.get(0).get(0).split(OzConstants.TILDE);
		String samAccountNamesString = "";
		for (String dn1 : dns) {
			logger.finest(dn1.concat("\n"));
			KeyValuePair<String, String> filter1 = new KeyValuePair<String, String>("distinguishedName", dn1);
			String[] attibutes2Get1 = { requiredAttribute };
			List<List<String>> samAccountNameList = LdapUtils.getAttributes(attibutes2Get1,
					filter1.getKey().concat(OzConstants.EQUAL_SIGN), filter1.getValue(), ldapCtx);
			samAccountNamesString = StringUtils.concat(samAccountNamesString, OzConstants.COMMA,
					samAccountNameList.get(0).get(0));
			logger.info(OzConstants.LINE_FEED
					.concat(ListUtils.getAsDelimitedString(samAccountNameList.get(0))));
		}
		logger.info(samAccountNamesString + "\nall done");
	}

	public static StringBuilder getRecursiveGroupMembershipByTokenGroup(final Properties properties) {
		StringBuilder recursiveGroupMembershipListSb = null;
		Hashtable env = new Hashtable();
		String bindUser = properties.getProperty("bindUser");
		String bindPassword = properties.getProperty("bindPassword");
		String ldapURL = properties.getProperty("ldapURL");
		String userSearchBase = properties.getProperty("userSearchBase");
		String groupsSearchBase = properties.getProperty("groupsSearchBase");

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		// set security credentials, note using simple cleartext authentication
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, bindUser);
		env.put(Context.SECURITY_CREDENTIALS, bindPassword);
		// connect to my domain controller
		env.put(Context.PROVIDER_URL, ldapURL);
		// specify attributes to be returned in binary format
		env.put("java.naming.ldap.attributes.binary", "tokenGroups");
		env.put("com.sun.jndi.ldap.trace.ber", System.err);
		try {
			// Create the initial directory context
			LdapContext ctx = new InitialLdapContext(env, null);
			// Create the search controls
			SearchControls userSearchCtls = new SearchControls();
			// Specify the search scope
			userSearchCtls.setSearchScope(SearchControls.OBJECT_SCOPE);
			// specify the LDAP search filter to find the user in question
			String userSearchFilter = "(objectClass=user)";
			// paceholder for an LDAP filter that will store SIDs of the groups
			// the user belongs to
			StringBuffer groupsSearchFilter = new StringBuffer();
			groupsSearchFilter.append("(|");
			// Specify the Base for the search
			// Specify the attributes to return
			String[] userReturnedAtts = { "tokenGroups" };
			userSearchCtls.setReturningAttributes(userReturnedAtts);
			// Search for objects using the filter
			NamingEnumeration userAnswer = ctx.search(userSearchBase, userSearchFilter, userSearchCtls);
			// Loop through the search results
			while (userAnswer.hasMoreElements()) {
				SearchResult sr = (SearchResult) userAnswer.next();
				Attributes attrs = sr.getAttributes();
				if (attrs != null) {
					try {
						for (NamingEnumeration ae = attrs.getAll(); ae.hasMore();) {
							Attribute attr = (Attribute) ae.next();
							for (NamingEnumeration e = attr.getAll(); e.hasMore();) {
								byte[] sid = (byte[]) e.next();
								groupsSearchFilter.append("(objectSid=" + LdapUtils.binarySidToStringSid(sid) + ")");
							}
							groupsSearchFilter.append(")");
						}
					} catch (NamingException nex) {
						ExceptionUtils.printMessageAndStackTrace(nex);
						System.err.println("Problem listing membership: " + nex);
					}
				}
			}
			logger.finest("************** " + groupsSearchFilter.toString());
			// Search for groups the user belongs to in order to get their names
			// Create the search controls
			SearchControls groupsSearchCtls = new SearchControls();
			// Specify the search scope
			groupsSearchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			// Specify the Base for the search
			// Specify the attributes to return
			String groupsReturnedAtts[] = { "sAMAccountName" };
			groupsSearchCtls.setReturningAttributes(groupsReturnedAtts);
			// Search for objects using the filter
			NamingEnumeration groupsAnswer = ctx.search(groupsSearchBase, groupsSearchFilter.toString(),
					groupsSearchCtls);

			// Loop through the search results
			recursiveGroupMembershipListSb = new StringBuilder();
			while (groupsAnswer.hasMoreElements()) {
				SearchResult sr = (SearchResult) groupsAnswer.next();
				Attributes attrs = sr.getAttributes();
				if (attrs != null) {
					recursiveGroupMembershipListSb.append(attrs.get("sAMAccountName").get() + "\n");
				}
			}
			logger.finest(recursiveGroupMembershipListSb.toString());
			ctx.close();
		} catch (NamingException nex) {
			ExceptionUtils.printMessageAndStackTrace(nex);
			System.err.println("Problem searching directory: " + nex);
		}
		return recursiveGroupMembershipListSb;
	}

	public static String getSmtpAddress(final String attribute, final String attributeValue,
			final LdapContext ldapCtx) {
		String smtpAddress = null;
		final String[] attrIDs = { LdapUtils.PROXY_ADDRESSES };
		List<List<String>> attributesList = LdapUtils.getAttributes(attrIDs, attribute, attributeValue, ldapCtx,
				LdapUtils.DEFAULT_FIBI_BASE_ENTRY_DN);
		if (attributesList != null && attributesList.size() > 0 && attributesList.get(0) != null
				&& attributesList.get(0).size() > 0) {
			String proxyAddresses = attributesList.get(0).get(0);
			smtpAddress = get1stSmtpAddressFromProxyAddresses(proxyAddresses);
		}
		return smtpAddress;
	}

	public static String[] getSmtpAddresses(final String attribute, final String[] attributeValues,
			final LdapContext ldapCtx) {
		String[] attributesNamesArray = ArrayUtils.populateArray(attribute, attributeValues.length);
		return LdapHelperUtils.getSmtpAddresses(attributesNamesArray, attributeValues, ldapCtx);
	}

	public static String[] getSmtpAddresses(final String[] attributesArray, final String[] attributeValues,
			final LdapContext ldapCtx) {
		String[] smtpAddressesArray = new String[attributesArray.length];
		for (int i = 0; i < attributesArray.length; i++) {
			smtpAddressesArray[i] = getSmtpAddress(attributesArray[i], attributeValues[i], ldapCtx);
		}
		return smtpAddressesArray;
	}
}
