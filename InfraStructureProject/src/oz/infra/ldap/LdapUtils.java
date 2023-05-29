package oz.infra.ldap;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
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
import oz.infra.map.MapUtils;
import oz.infra.system.SystemPropertiesUtils;

public class LdapUtils {
	private static final String LDAP_DEFAULT_PROTOCOL = "ldap";
	private static final String LDAPS_DEFAULT_PROTOCOL = "ldaps";
	private static final String LDAP_DEFAULT_PORT = "389";
	private static final String LDAPS_DEFAULT_PORT = "636";
	private static final String SECURITY_PROTOCOL_TLS = "tls";
	private static final String FIBI_CORP_DOMAIN = "fibi.corp";
	static final String DEFAULT_FIBI_BASE_ENTRY_DN = "DC=fibi,DC=corp";

	public static final String DISPLAY_NAME = "displayName";
	public static final String MAIL = "mail";
	public static final String NAME = "name";
	public static final String PROXY_ADDRESSES = "proxyAddresses";
	public static final String SAM_ACCOUNT_NAME = "sAMAccountName";

	private static Logger logger = JulUtils.getLogger();

	public static final String binarySidToStringSid(final byte[] SID) {
		String strSID = "";
		// convert the SID into string format
		long version;
		long authority;
		long count;
		long rid;

		strSID = "S";
		version = SID[0];
		strSID = strSID + "-" + Long.toString(version);
		authority = SID[4];

		for (int i = 0; i < 4; i++) {
			authority <<= 8;
			authority += SID[4 + i] & 0xFF;
		}

		strSID = strSID + "-" + Long.toString(authority);
		count = SID[2];
		count <<= 8;
		count += SID[1] & 0xFF;

		for (int j = 0; j < count; j++) {
			rid = SID[11 + (j * 4)] & 0xFF;
			for (int k = 1; k < 4; k++) {
				rid <<= 8;
				rid += SID[11 - k + (j * 4)] & 0xFF;
			}
			strSID = strSID + "-" + Long.toString(rid);
		}
		return strSID;
	}

	public static List<List<String>> getAttributes(final String attrID, final String searchAttributeName,
			final String searchAttributeValue, final LdapContext ldapCtx, final String baseEntryDn) {
		String[] attrIDs = { attrID };
		String filter = searchAttributeName + searchAttributeValue;
		return getAttributes(attrIDs, filter, ldapCtx, baseEntryDn);
	}

	public static List<List<String>> getAttributes(final String[] attrIDs, final String filter,
			final LdapContext ldapCtx) {
		return getAttributes(attrIDs, filter, ldapCtx, DEFAULT_FIBI_BASE_ENTRY_DN);
	}

	public static List<List<String>> getAttributes(final String[] attrIDs, final String filter,
			final LdapContext ldapCtx, final String baseEntryDn) {
		logger.finest("filter: " + filter);
		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			constraints.setReturningAttributes(attrIDs);
			NamingEnumeration<SearchResult> searchResultEnumeration = ldapCtx.search(baseEntryDn, filter, constraints);
			return processSearchResult(searchResultEnumeration, filter);
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}

	public static List<List<String>> getAttributes(final String[] attrIDs, final String searchAttributeName,
			final String searchAttributeValue, final LdapContext ldapCtx) {
		return getAttributes(attrIDs, searchAttributeName, searchAttributeValue, ldapCtx, DEFAULT_FIBI_BASE_ENTRY_DN);
	}

	public static List<List<String>> getAttributes(final String[] attrIDs, final String searchAttributeName,
			final String searchAttributeValue, LdapContext ldapCtx, final String baseEntryDn) {
		String filter = searchAttributeName + searchAttributeValue;
		return getAttributes(attrIDs, filter, ldapCtx, baseEntryDn);
	}

	public static String getDefaultBaseEntryDn() {
		return DEFAULT_FIBI_BASE_ENTRY_DN;
	}

	private static Hashtable<String, Object> getEnvHashTable(final String userName, final String password,
			final String domain, final String ldapServer, final String protocol, final String port) {
		Hashtable<String, Object> env = new Hashtable<String, Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "Simple");
		String securityPrincipal = userName + "@" + domain;
		env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.PROVIDER_URL, protocol + "://" + ldapServer + ":" + port);
		env.put(Context.REFERRAL, "follow");
		env.put("com.sun.jndi.ldap.trace.ber", System.err);
		return env;
	}

	public static LdapContext getLdapContext(Hashtable<String, Object> env) {
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

	public static LdapContext getLdapContext(final String userName, final String password) {
		return getLdapContext(userName, password, FIBI_CORP_DOMAIN, FIBI_CORP_DOMAIN);
	}

	public static LdapContext getLdapContext(final String userName, final String password, final String domain,
			final String ldapServer) {
		Hashtable<String, Object> env = getEnvHashTable(userName, password, domain, ldapServer, LDAP_DEFAULT_PROTOCOL,
				LDAP_DEFAULT_PORT);
		return getLdapContext(env);
	}

	public static LdapContext getSecureLdapContext(final String userName, final String password, final String domain,
			final String ldapServer, final String trustStoreFilePath, final String trustStorePassword) {
		System.setProperty(SystemPropertiesUtils.SYSTEM_PROPERTY_TRUST_STORE, trustStoreFilePath);
		System.setProperty(SystemPropertiesUtils.SYSTEM_PROPERTY_TRUST_STORE_PASSWORD, trustStorePassword);
		String protocol = LDAPS_DEFAULT_PROTOCOL;
		String port = LDAPS_DEFAULT_PORT;
		Hashtable<String, Object> env = getEnvHashTable(userName, password, domain, ldapServer, protocol, port);
		env.put(Context.SECURITY_PROTOCOL, SECURITY_PROTOCOL_TLS);
		return getLdapContext(env);
	}

	private static List<List<String>> processSearchResult(final NamingEnumeration<SearchResult> searchResultEnumeration,
			final String filter) {
		final String sbDelimiter = OzConstants.TILDE;
		Level level = Level.FINEST;
		List<List<String>> resultList = new ArrayList<List<String>>();
		try {
			if (!searchResultEnumeration.hasMore()) {
				logger.warning("*** Search result is empty *** filter: " + filter);
			} else {
				logger.log(level, "Start outer while loop on searchResultEnumeration");
				while (searchResultEnumeration.hasMore()) {
					logger.log(level, "inside outer while loop");
					SearchResult searchResult = searchResultEnumeration.next();
					logger.log(level, "+++ CN: " + searchResult.getNameInNamespace());
					logger.log(level, "+++ Name: " + searchResult.getName());
					Attributes attributes = searchResult.getAttributes();
					NamingEnumeration ne = attributes.getAll();
					List<String> attributesList = new ArrayList<String>();
					while (ne.hasMore()) {
						Attribute attribute1 = (Attribute) ne.next();
						logger.log(level, "+++ Attribute name: " + attribute1.getID());
						// resultEntrySb.append(at1.getID());
						// resultEntrySb.append(OzConstants.EQUAL_SIGN);
						NamingEnumeration ne1 = attribute1.getAll();
						logger.log(level, "Start inner while loop");
						StringBuilder resultEntrySb = new StringBuilder();
						while (ne1.hasMore()) {
							if (resultEntrySb.length() > 0) {
								resultEntrySb.append(sbDelimiter);
							}
							logger.log(level, "inside inner while loop");
							Object obj = ne1.next();
							logger.log(level,
									"Attribute name: " + attribute1.getID() + " Attribute value: " + obj.toString());
							resultEntrySb.append(obj.toString());

						}
						attributesList.add(resultEntrySb.toString());
					}
					resultList.add(attributesList);
				}
			}
		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		ListUtils.printListOfLists(resultList, "title", Level.INFO, OzConstants.COMMA);
		return resultList;
	}

	private static String[] processSearchResult(final String[] attrIDs,
			NamingEnumeration<SearchResult> searchResultEnumeration) {
		String[] response = new String[attrIDs.length];
		try {
			if (searchResultEnumeration.hasMore()) {
				logger.finest("outer loop ...");
				Attributes attributes = searchResultEnumeration.next().getAttributes();
				for (int i = 0; i < attrIDs.length; i++) {
					Attribute attribute = attributes.get(attrIDs[i]);
					response[i] = attribute.get(0).toString();
					logger.finest(response[i]);
				}
			} else {
				logger.warning("getAttributes has encountered a !");
				throw new Exception("getAttributes problem");
			}
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			ex.printStackTrace();
		}
		ArrayUtils.printArray(response, Level.FINEST);
		return response;
	}
}
