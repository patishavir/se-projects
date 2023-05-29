package oz.infra.spnego;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

import oz.infra.base64.Base64Utils;
import oz.infra.constants.OzConstants;
import oz.infra.exception.ExceptionUtils;
import oz.infra.http.URLBreakDown;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.infra.varargs.VarArgsUtils;

public class SpnegoUtils {

	/*
	 * krb5.conf ========= [libdefaults] default_realm = FIBI.CORP [realms] FIBI.CORP = { kdc = fibildap1.fibi.corp:88 }
	 * 
	 * login.conf ========== com.sun.security.jgss.krb5.initiate { com.sun.security.auth.module.Krb5LoginModule required
	 * doNotPrompt=false useTicketCache=true; };
	 */

	/**
	 * @param args
	 */
	private static String jaasLoginConfPath = null;
	private static String domain = null;
	private static String spn = null;
	private static String targetUrl = null;
	private static String krb5Debug = null;
	private static String krb5ConfPath = null;
	private static String rootFolderPath = null;
	private static String kdc = null;
	private static final Logger logger = JulUtils.getLogger();

	public static String getProtectedPageContents(final String propertiesFilePath, final String targetUrl) {
		logger.info(SystemUtils.getRunInfo());
		SpnegoUtils.targetUrl = targetUrl;
		logger.info("target Url: " + targetUrl);
		processParameters(propertiesFilePath);
		String pageContents = null;
		try {
			setSystemProperties();
			Oid krb5MechOid = new Oid(OzConstants.KRB5_MECH_OID);
			Oid spnegoMechOid = new Oid(OzConstants.SPNEGO_MECH_OID);

			GSSManager manager = GSSManager.getInstance();
			GSSCredential clientGssCreds = null;
			clientGssCreds = manager.createCredential(null, GSSCredential.INDEFINITE_LIFETIME, krb5MechOid, GSSCredential.INITIATE_ONLY);
			logger.info("got clientGssCreds");
			clientGssCreds.add(null, GSSCredential.INDEFINITE_LIFETIME, GSSCredential.INDEFINITE_LIFETIME, spnegoMechOid,
					GSSCredential.INITIATE_ONLY);
			logger.info("clientGssCreds.add has completed");
			// Next Step: create target server SPN
			String targetServerSpn = spn;

			GSSName gssServerName = manager.createName(targetServerSpn, GSSName.NT_USER_NAME);
			logger.info("gssServerName has been created: " + gssServerName.toString());
			GSSContext clientContext = manager.createContext(gssServerName.canonicalize(spnegoMechOid), spnegoMechOid, clientGssCreds,
					GSSContext.DEFAULT_LIFETIME);
			logger.info("clientContext has been created");
			// optional enable GSS credential delegation
			clientContext.requestCredDeleg(true);

			byte[] spnegoToken = new byte[0];

			// create a SPNEGO token for the target server
			spnegoToken = clientContext.initSecContext(spnegoToken, 0, spnegoToken.length);
			logger.info("spnegoToken.length: " + String.valueOf(spnegoToken.length));

			String negotiate = Base64Utils.encode(spnegoToken);
			logger.info("Got Negotiate: " + negotiate);

			URL url = new URL(targetUrl);
			logger.finest("\ntry to access " + url.toString() + "\n");

			HttpURLConnection con = null;
			con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			// con.setAllowUserInteraction(true);
			con.setRequestProperty("Authorization", "Negotiate " + negotiate);
			String line = null;

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuilder sb = new StringBuilder("\n");
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			sb.append("\nEnd");
			pageContents = sb.toString();
			logger.info(pageContents);
			logger.info("returning page contents. page length: " + String.valueOf(pageContents.length()));

		} catch (Exception ex) {
			ExceptionUtils.printMessageAndStackTrace(ex);
		}
		return pageContents;
	}

	private static void processParameters(final String propertiesFilePath) {
		rootFolderPath = PathUtils.getParentFolderPath(propertiesFilePath);
		rootFolderPath = new File(rootFolderPath).getAbsolutePath();
		ParametersUtils.processPatameters(propertiesFilePath, SpnegoUtils.class);
		setSpn();
		FileUtils.exitIfFileDoesNoteExist(jaasLoginConfPath);
		FileUtils.exitIfFileDoesNoteExist(krb5ConfPath);
	}

	public static void setDomain(final String domain) {
		SpnegoUtils.domain = domain;
	}

	public static void setJaasLoginConfPath(final String jaasLoginConfPath) {
		SpnegoUtils.jaasLoginConfPath = PathUtils.getFullPath(rootFolderPath, jaasLoginConfPath);
	}

	public static void setKdc(final String kdc) {
		SpnegoUtils.kdc = kdc;
	}

	public static void setKrb5ConfPath(final String krb5ConfPath) {
		SpnegoUtils.krb5ConfPath = PathUtils.getFullPath(rootFolderPath, krb5ConfPath);
	}

	public static void setKrb5Debug(final String krb5Debug) {
		SpnegoUtils.krb5Debug = krb5Debug;
	}

	public static void setSpn() {
		if (spn == null) {
			URLBreakDown urlBreakDown = new URLBreakDown(targetUrl);
			spn = "HTTP/" + urlBreakDown.getHost() + OzConstants.AT_SIGN + domain;
			logger.info("generated spn: " + spn);
		}
	}

	public static void setSpn(final String spn) {
		SpnegoUtils.spn = spn;
	}

	private static void setSystemProperties() {
		System.setProperty(SystemUtils.SUN_SECURITY_KRB5_DEBUG, krb5Debug);
		System.setProperty(SystemUtils.JAVAX_SECURITY_AUTH_USESUBJECTCREDSONLY, "false");

		System.setProperty(SystemUtils.JAVA_SECURITY_KRB5_CONF, krb5ConfPath);
		System.setProperty(SystemUtils.JAVA_SECURITY_KRB5_REALM, "FIBI.CORP");
		System.setProperty(SystemUtils.JAVA_SECURITY_KRB5_KDC, kdc);

		System.setProperty(SystemUtils.JAVA_SECURITY_AUTH_LOGIN_CONFIG, jaasLoginConfPath);
		SystemPropertiesUtils.printSystemProperties("java.sec");

		logger.finest(
				"make sure that registry key: HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Control\\Lsa\\Kerberos\\Parameters\\AllowTGTSessionKey = (REG_DWORD) 1");

	}

	public static Properties getSpengoDefaultProperties(final String... domains) {
		String domain = VarArgsUtils.getMyArg("FIBI.CORP", domains);
		Properties spengoDefaultProperties = new Properties();
		spengoDefaultProperties.setProperty("jaasLoginConfPath", "login.conf");
		spengoDefaultProperties.setProperty("krb5Debug", "true");
		spengoDefaultProperties.setProperty("krb5ConfPath", "krb5.conf");
		spengoDefaultProperties.setProperty("jaasLoginConfPath", "login.conf");
		spengoDefaultProperties.setProperty("kdc", "88");
		spengoDefaultProperties.setProperty("domain", domain);
		return spengoDefaultProperties;
	}

	public static void setRootFolderPath(final String rootFolderPath) {
		SpnegoUtils.rootFolderPath = rootFolderPath;
	}
}
