package oz.utils.cm.ds.common;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import oz.infra.constants.OzConstants;
import oz.infra.fibi.gm.GMUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.map.MapUtils;
import oz.infra.string.StringUtils;
import oz.infra.system.SystemPropertiesUtils;
import oz.infra.system.SystemUtils;
import oz.utils.cm.ds.DsCmRunParameters;

public class DsCmEmailUtils {
	private static final Logger logger = JulUtils.getLogger();

	public static String getMailRecepientFromFilePath(final String filepath) {
		String[] dsxFilePathBreakdown = filepath.split(DsCmRunParameters.getFileNameBreakdownDelimiter());
		String userName = dsxFilePathBreakdown[1];
		int indexOfDot = userName.indexOf(OzConstants.DOT);
		if (indexOfDot > OzConstants.STRING_NOT_FOUND) {
			userName = userName.substring(0, indexOfDot);
		}
		return userName;
	}

	public static void processEmail(final String filepath, final String messageText, final Map<String, String> emailMap) {
		String userName = getMailRecepientFromFilePath(filepath);
		logger.info("userName: ".concat(userName));
		String curentEmailMessage = emailMap.get(userName);
		if (curentEmailMessage == null) {
			curentEmailMessage = "";
		}
		String newMailMessage = StringUtils.concat(curentEmailMessage, messageText,
				StringUtils.repeatString(OzConstants.UNDERSCORE, OzConstants.INT_128));
		emailMap.put(userName, newMailMessage);
	}

	public static void sendEmail1(final String body, final String subject, final String recepient, final String addtionalRecepients) {
		Properties gmMailProperties = GMUtils.getGmEmailDefaultProperties();
		StringBuilder sb = new StringBuilder();
		sb.append("<HTML><BODY>");
		sb.append(body);
		sb.append("</BODY></HTML>");
		gmMailProperties.put(GMUtils.HTML_BODY, sb.toString());
		String to = recepient;
		if (addtionalRecepients != null && addtionalRecepients.length() > 0) {
			to = StringUtils.concat(to, OzConstants.COMMA, addtionalRecepients);
		}
		gmMailProperties.put(GMUtils.TO, to);
		String fullSubject = StringUtils.concat(subject, recepient, ". Environment: ", DsCmRunParameters.getEnvironment(),
				" Ran by: " + SystemPropertiesUtils.getUserName() + " on " + SystemUtils.getHostname());
		gmMailProperties.put(GMUtils.SUBJECT, fullSubject);
		gmMailProperties.put(GMUtils.GM_ENVIRONMENT, GMUtils.GM_PROD_ENVIRONMENT);
		GMUtils.sendEmail(gmMailProperties);
	}

	public static void sendEmails(final Map<String, String> emailMap, final String subject, final String addtionalRecepients) {
		MapUtils.printMap(emailMap, "email map", Level.FINEST);
		Set<String> recepients = emailMap.keySet();
		for (String recepient : recepients) {
			String body = emailMap.get(recepient);
			sendEmail1(body, subject, recepient, addtionalRecepients);
		}
	}
}
