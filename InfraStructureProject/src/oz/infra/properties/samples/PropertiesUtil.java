package oz.infra.properties.samples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtil {
	public static final int SET = 1;
	public static final int DEL = 2;
	public static final int LIST = 3;

	void process(final File propertiesFile, final String actionString, final String key,
			final String value) {
		InputStream input;
		OutputStream output;
		try {
			Properties properties = new Properties();
			if (!propertiesFile.exists()) {
				System.out.println(propertiesFile.getAbsolutePath()
						+ " does not exist.  Creating file.");
				propertiesFile.createNewFile();
			}
			if (!propertiesFile.exists()) {
				String fileNotFoundMessage = propertiesFile.getAbsolutePath() + " does not exist!";
				System.err.println(fileNotFoundMessage);
				throw new RuntimeException(fileNotFoundMessage);
			}
			properties.load(input = new FileInputStream(propertiesFile));
			input.close();
			if (actionString.equalsIgnoreCase("set")) {
				properties.setProperty(key, value);
			} else if (actionString.equalsIgnoreCase("del")) {
				properties.remove(key);
			} else if (actionString.equalsIgnoreCase("list")) {
				properties.list(System.out);
			}
			if (!actionString.equalsIgnoreCase("list")) {
				properties.store(output = new FileOutputStream(propertiesFile), null);
				output.close();
			}
			System.out.println(actionString + " operation completed successfuly for "
					+ propertiesFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}
