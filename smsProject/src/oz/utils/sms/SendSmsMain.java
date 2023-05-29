package oz.utils.sms;

import oz.infra.locale.LocaleUtils;

public class SendSmsMain {

	public static void main(final String[] args) {
		LocaleUtils.getDefaultLocale();
		SendSmsParameters.processParameters(args);
	}
}
