package oz.zel;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.zel.file.Csv2XmlConversion;

public class ZelMain {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Xls2XmlConversion.doJdbcProceesning();
		Csv2XmlConversion.processFile();

	}

}
