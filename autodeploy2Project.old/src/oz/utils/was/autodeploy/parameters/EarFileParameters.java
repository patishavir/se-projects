package oz.utils.was.autodeploy.parameters;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.io.FileUtils;
import oz.infra.io.PathUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.parameters.ParametersUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.string.StringUtils;

public class EarFileParameters {

	private static final Logger logger = JulUtils.getLogger();
	private String cluster2Restart = null;
	private String gmMailTo = null;
	private String gmMailFrom = null;
	private String smtpMailTo = null;
	private String smtpMailFrom = null;
	private String applicationName = null;
	private String jythonScriptSourceFilePath = null;
	private String jythonScriptTargetFilePath = null;
	private String system = null;
	private String remotePropertiesFilePath = null;
	private Properties remoteProperties = null;
	private String emailMessagesPropertiesFilePath = null;
	private Properties emailMessagesProperties = null;
	private int returnCode = Integer.MAX_VALUE;
	private String getSnifitVersionParameters = null;
	private String[] getSnifitVersionParametersArray = null;
	private String snifitVersion = "";
	private String user = null;
	private String password = null;
	private String encryptionMethod = null;
	private String rootFolderPath = null;

	public String getApplicationName() {
		return applicationName;
	}

	public String getCluster2Restart() {
		return cluster2Restart;
	}

	public Properties getEmailMessagesProperties() {
		return emailMessagesProperties;
	}

	public String getEmailMessagesPropertiesFilePath() {
		return emailMessagesPropertiesFilePath;
	}

	public String getEncryptionMethod() {
		return encryptionMethod;
	}

	public String[] getGetSnifitVersionParametersArray() {
		return getSnifitVersionParametersArray;
	}

	public String getGmMailFrom() {
		return gmMailFrom;
	}

	public String getGmMailTo() {
		return gmMailTo;
	}

	public String getJythonScriptSourceFilePath() {
		return jythonScriptSourceFilePath;
	}

	public String getJythonScriptTargetFilePath() {
		return jythonScriptTargetFilePath;
	}

	public String getPassword() {
		return password;
	}

	public Properties getRemoteProperties() {
		return remoteProperties;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public String getSmtpMailFrom() {
		return smtpMailFrom;
	}

	public String getSmtpMailTo() {
		return smtpMailTo;
	}

	public String getSnifitVersion() {
		return snifitVersion;
	}

	public String getSystem() {
		return system;
	}

	public String getUser() {
		return user;
	}

	public void processParameters(final String propertiesFilePath) {
		ParametersUtils.processPatameters(propertiesFilePath, this);
	}

	public void setApplicationName(final String applicationName) {
		this.applicationName = applicationName;
	}

	public void setCluster2Restart(final String cluster2Restart) {
		this.cluster2Restart = cluster2Restart;
	}

	public void setEmailMessagesPropertiesFilePath(final String emailMessagesPropertiesFilePath) {
		this.emailMessagesPropertiesFilePath = PathUtils.getFullPath(rootFolderPath, emailMessagesPropertiesFilePath);
		emailMessagesProperties = PropertiesUtils.loadPropertiesFile(this.emailMessagesPropertiesFilePath,
				OzConstants.WIN1255_ENCODING);
	}

	public void setEncryptionMethod(String encryptionMethod) {
		this.encryptionMethod = encryptionMethod;
	}

	public void setGetSnifitVersionParameters(final String getSnifitVersionParameters) {
		getSnifitVersionParametersArray = getSnifitVersionParameters.split(OzConstants.COMMA);
		getSnifitVersionParametersArray[0] = PathUtils.getFullPath(rootFolderPath, getSnifitVersionParametersArray[0]);
	}

	public void setGmMailFrom(final String gmMailFrom) {
		this.gmMailFrom = gmMailFrom;
	}

	public void setGmMailTo(final String gmMailTo) {
		this.gmMailTo = gmMailTo;
	}

	public void setJythonScriptSourceFilePath(final String jythonScriptSourceFilePath) {
		this.jythonScriptSourceFilePath = PathUtils.getFullPath(rootFolderPath, jythonScriptSourceFilePath);
		FileUtils.terminateIfFileDoesNotExist(this.jythonScriptSourceFilePath);
	}

	public void setJythonScriptTargetFilePath(final String jythonScriptTargetFilePath) {
		this.jythonScriptTargetFilePath = jythonScriptTargetFilePath;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRemotePropertiesFilePath(final String remotePropertiesFilePath) {
		String remotePropertiesFileFullPath = PathUtils.getFullPath(rootFolderPath, remotePropertiesFilePath);
		FileUtils.terminateIfFileDoesNotExist(remotePropertiesFileFullPath);
		logger.info(StringUtils.concat("remotePropertiesFile full path: ", remotePropertiesFileFullPath));
		remoteProperties = PropertiesUtils.loadPropertiesFile(remotePropertiesFileFullPath);
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public void setRootFolderPath(final String rootFolderPath) {
		this.rootFolderPath = rootFolderPath;
	}

	public void setSmtpMailFrom(final String smtpMailFrom) {
		this.smtpMailFrom = smtpMailFrom;
	}

	public void setSmtpMailTo(final String smtpMailTo) {
		this.smtpMailTo = smtpMailTo;
	}

	public void setSnifitVersion(final String snifitVersion) {
		this.snifitVersion = snifitVersion;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
