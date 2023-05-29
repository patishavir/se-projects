package oz.ssh.run;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.collection.CollectionUtils;
import oz.infra.constants.OzConstants;
import oz.infra.cryptography.CryptographyUtils;
import oz.infra.cryptography.EncryptionMethodEnum;
import oz.infra.datetime.StopWatch;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;
import oz.infra.properties.PropertiesUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.ssh.RunRemoteCommand3;
import oz.infra.ssh.jsch.JschUtils;
import oz.infra.string.StringUtils;
import oz.infra.swing.joptionpane.JOptionPaneUtils;
import oz.infra.system.SystemUtils;

public class RunSshHandler implements Observer {
	private static enum ParametersEnum {
		COMMAND, ALL_SERVERS_PROD, ALL_WAS_SERVERS_PROD, ALL_SERVERS_TEST, ALL_WAS_SERVERS_TEST, SERVER
	};

	private static final String PREFIX_SUFFIX = "************";
	private static Set<String> serversSet = new HashSet<String>();
	private static String command = null;
	private static final String LF = SystemUtils.LINE_SEPARATOR;
	private static Properties serverUserPassproperties = PropertiesUtils.loadPropertiesFromClassPath("oz/ssh/data/server-userpass.properties");
	private static Map<String, String> dynamicUserPasswordMap = new HashMap<String, String>();
	private static Logger logger = JulUtils.getLogger();

	private static void addSetversToServersSet(final String[] serversArray) {
		for (String server1 : serversArray) {
			logger.finest(server1);
			serversSet.add(server1);
		}
	}

	private static UserNamePassword getUserNamePassword(final String server1) {
		PropertiesUtils.printProperties(serverUserPassproperties, Level.FINEST);
		String userPass = serverUserPassproperties.getProperty(server1);
		String[] userPassArray = userPass.split(OzConstants.COMMA);
		String userName = userPassArray[0];
		String password = null;
		boolean decryptionRequired = true;
		if (userPassArray.length > 1) {
			password = userPassArray[1];
		} else {
			password = dynamicUserPasswordMap.get(userName);
			if (password == null) {
				password = JOptionPaneUtils.showPasswordDialog("enter password for: " + userName, "password for " + userName);
				dynamicUserPasswordMap.put(userName, password);
			}
			decryptionRequired = false;
		}
		return new UserNamePassword(userName, password, decryptionRequired);
	}

	private static void runCommand() {
		StopWatch stopWatch = new StopWatch();
		int maxReturnCode = Integer.MIN_VALUE;
		int returnCode = 0;
		StringBuilder sb = new StringBuilder(LF);
		for (String server1 : serversSet) {
			String startRunMessage = StringUtils.concat(PREFIX_SUFFIX, " Start running ", command, " on ", server1, " ", PREFIX_SUFFIX, LF, LF);
			sb.append(startRunMessage);
			SystemCommandExecutorResponse scer = JschUtils.exec(command, server1, Level.FINEST);
			sb.append(scer.getStdout());
			String stdErr = scer.getStderr();
			if (stdErr != null && stdErr.length() > 0) {
				sb.append(stdErr);
			}
			String completionMessage = StringUtils.concat(LF, PREFIX_SUFFIX, " ", command, " on ", server1, " has completed. Return code: ",
					String.valueOf(returnCode), " ", PREFIX_SUFFIX);
			sb.append(StringUtils.concat(completionMessage, LF, LF));
			if (returnCode > maxReturnCode) {
				maxReturnCode = returnCode;
			}
		}
		String stopMessage = StringUtils.concat(LF, "All done ! ", String.valueOf(serversSet.size()),
				" servers have been processed. Max return code: ", String.valueOf(maxReturnCode), LF, "Duration: ", stopWatch.getElapsedTimeString());
		sb.append(stopMessage);
		logger.info(sb.toString());
	}

	private static SystemCommandExecutorResponse runCommand1Server(final String server1) {
		int returnCode = 0;
		UserNamePassword userNamePassword = getUserNamePassword(server1);
		String userName = userNamePassword.getUserName();
		String password = userNamePassword.getPassword();
		SystemCommandExecutorResponse scer = null;
		if (password != null) {
			if (userNamePassword.isDecryptionRequired()) {
				password = CryptographyUtils.decryptString(password, EncryptionMethodEnum.WITH_HOSTNAME_AND_USERNAME);
			}
			String[] parametersArray = { server1, userName, password, command };
			String startRunMessage = StringUtils.concat(PREFIX_SUFFIX, " Start running ", command, " on ", server1, ". user: ", userName, " ",
					PREFIX_SUFFIX, LF);
			System.out.println(startRunMessage);
			scer = RunRemoteCommand3.runRemoteCommand(parametersArray);
			returnCode = scer.getReturnCode();
			if (returnCode == -1) {
				SystemUtils.printMessageAndExit("wrong password for " + userName + ". processing has been terminated.", returnCode, true);
			}
		} else {
			String noPasswordMessage = "Password for " + userName + " on server " + server1 + " is not defined. Command has not been run !";
			logger.warning(noPasswordMessage);
		}
		return scer;
	}

	private static void processParameters(final ParametersEnum parametersEnum, final Map.Entry<String, String> mapEntry1) {
		logger.finest(
				StringUtils.concat("parametersEnum: ", parametersEnum.toString(), " key: ", mapEntry1.getKey(), " value: ", mapEntry1.getValue()));
		switch (parametersEnum) {
		case COMMAND:
			command = mapEntry1.getValue();
			break;
		case ALL_SERVERS_PROD:
		case ALL_SERVERS_TEST:
		case ALL_WAS_SERVERS_TEST:
		case ALL_WAS_SERVERS_PROD:
			if (mapEntry1.getValue().equalsIgnoreCase(OzConstants.YES)) {
				String[] servers = FileUtils.readTextFileFromClassPath("/oz/ssh/data/" + parametersEnum).split(LF);
				addSetversToServersSet(servers);
			}
			break;
		case SERVER:
			if (mapEntry1.getValue().equalsIgnoreCase(OzConstants.YES)) {
				addSetversToServersSet(mapEntry1.getKey().split(LF));
			}
			break;
		}
	}

	@Override
	public final void update(final Observable observable, final Object parametersHashTableObj) {
		Hashtable<String, String> parametersHashTable = (Hashtable<String, String>) parametersHashTableObj;
		MapUtils.printMap(parametersHashTable, Level.FINEST);
		Set<Map.Entry<String, String>> parametersSet = parametersHashTable.entrySet();
		for (Map.Entry<String, String> mapEntry1 : parametersSet) {
			logger.finest(mapEntry1.getKey() + OzConstants.EQUAL_SIGN + mapEntry1.getValue());
			String key = mapEntry1.getKey();
			ParametersEnum parametersEnum = null;
			try {
				parametersEnum = ParametersEnum.valueOf(key);
			} catch (IllegalArgumentException ex) {
				parametersEnum = ParametersEnum.SERVER;
			}
			processParameters(parametersEnum, mapEntry1);
		}
		CollectionUtils.printCollection(serversSet, Level.FINEST);
		//
		runCommand();
		System.exit(0);
	}
}
