package oz.utils.mq;

import java.util.Properties;

public class MQUtils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Properties properties = oz.infra.properties.PropertiesUtils.loadPropertiesFile(args[0]);
		putMessage(properties);
	}

	public static void putMessage(final Properties putProperties) {
		oz.infra.mq.MQUtils.putMessage(putProperties);

	}

}
