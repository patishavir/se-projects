package oz.infra.bidi.test;

import java.util.logging.Logger;

import oz.infra.bidi.BidiUtils;
import oz.infra.logging.jul.JulUtils;

public class TestBidiUtils {
	private static final Logger logger = JulUtils.getLogger();

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String str = null;
		str = "INSERT INTO GLST_SGIA_MISHNI (GL_ZIHUY_HODAA,GL_HODAA,GL_MIVNE_HALON_HODAA,GL_HUMRA_HODAA,GL_KOD_TIPUL) VALUES('MP1005','׳�׳� ׳”׳•׳©׳�׳�׳• ׳›׳� ׳₪׳¨׳˜׳™ ׳”׳”׳�׳•׳•׳�׳”. ׳—׳¡׳¨׳™׳� ׳ ׳×׳•׳ ׳™ ׳¡׳“׳¨׳•׳× ׳×׳©׳�׳•׳�׳™׳�.','MT','S','S')";
		testConvert(str);
		str = "123 XYZqaz !@#";
		testConvert(str);
		str = "INSERT INTO GLST_SGIA_MISHNI (GL_ZIHUY_HODAA, GL_HODAA, GL_MIVNE_HALON_HODAA, GL_HUMRA_HODAA, GL_KOD_TIPUL) VALUES ('NE1022', '׳�׳™׳� ׳�׳©׳ ׳•׳× ׳¡׳•׳’ ׳”׳•׳¨׳�׳” ׳� STP/STL', 'MS', 'S', 'D')";
		testConvert(str);
		str = "INSERT INTO CMST_SUG_TADIRUT (CM_SUG_TADIRUT, CM_SUG_TADIRUT_TEUR) VALUES ('1', '׳™׳�׳™׳�');";
		testConvert(str);
	}

	private static void testConvert(final String str) {
		logger.info(str);
		try {
			String str1 = BidiUtils.convertV2L(str);
			logger.info(str1);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning(ex.getMessage());
		}
	}
}
