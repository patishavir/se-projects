package oz.utils.sms.gui;

import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import oz.guigenerator.GuiGeneratorDefaultsProviderInterface;
import oz.guigenerator.GuiGeneratorMain;
import oz.infra.array.ArrayUtils;
import oz.infra.io.FileUtils;
import oz.infra.logging.jul.JulUtils;
import oz.infra.regexp.RegexpUtils;
import oz.infra.sms.SmsMessage;
import oz.utils.sms.SmsUtils;

public class SendSmsGui implements Observer, GuiGeneratorDefaultsProviderInterface {
	private static final String GUI_XML_PATH = "/oz/utils/sms/gui/xml/smsGui.xml";
	private SmsMessage smsParameters = null;
	private String toFilePath = null;
	private static Logger logger = JulUtils.getLogger();

	public final String getDefaultValue(final String key) {
		String value = null;
		return value;
	}

	public final String[] getValues(final String key) {
		logger.finest("key: " + key);
		String[] toArray = null;
		if (key.equals("to")) {
			if (toFilePath != null) {
				toArray = FileUtils.readTextFile2Array(toFilePath);
				logger.finest("toArray length: " + String.valueOf(toArray.length));
				toArray = ArrayUtils.selectArrayRowsByRegExpression(toArray, RegexpUtils.REGEXP_NON_WHITE_SPACE, true);
				logger.finest("toArray length(after whitespace removal): " + String.valueOf(toArray.length));
			}
			return toArray;
		} else {
			return null;
		}
	}

	public final void showGui(final SmsMessage smsParameters, final String toFilePath) {
		logger.finest(this.getClass().getPackage().toString());
		this.smsParameters = smsParameters;
		this.toFilePath = toFilePath;
		GuiGeneratorMain.showGui(null, GUI_XML_PATH, this, this, null);
	}

	public final void update(final Observable observable, final Object parametersHashTableObj) {
		Hashtable<String, String> smsParametersHashTable = (Hashtable<String, String>) parametersHashTableObj;
		String[] phoneNumberArray = smsParametersHashTable.get("to").split(":");
		smsParameters.setTo(phoneNumberArray[phoneNumberArray.length - 1].trim());
		smsParameters.setSmsMessageText(smsParametersHashTable.get("messageText"));
		SmsUtils.sendSms(smsParameters);
		System.exit(0);
	}
}
