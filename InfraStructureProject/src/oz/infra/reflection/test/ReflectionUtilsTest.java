package oz.infra.reflection.test;

import java.util.Properties;
import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.string.StringUtils;

public class ReflectionUtilsTest {
	private static String skey1 = null;
	private static String skey2 = null;
	private static String skey3 = null;
	private static int sintKey1 = Integer.MIN_VALUE;
	private static boolean sboolKey1 = false;
	private static Properties staticProperties = new Properties();
	private String ikey1 = null;

	private String ikey2 = null;

	private String ikey3 = null;

	private int iintKey1 = Integer.MIN_VALUE;

	private String remotePropertiesFilePath = "localFilePath";

	private boolean isboolKey1 = false;

	private Properties instanceProperties = new Properties();

	private String instanceAttribute = null;
	private static String staticAttribute = null;
	private static Logger logger = JulUtils.getLogger();

	static {
		staticProperties.put("skey1", "svalue1");
		staticProperties.put("skey2", "svalue2");
		staticProperties.put("skey3", "svalue3");
		staticProperties.put("sintKey1", "77899");
		staticProperties.put("sboolKey1", "yes");
	}

	public static int getSintKey1() {
		return sintKey1;
	}

	public static String getSkey1() {
		return skey1;
	}

	public static String getSkey2() {
		return skey2;
	}

	public static String getSkey3() {
		return skey3;
	}

	public static boolean isSboolKey1() {
		return sboolKey1;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// new ReflectionUtilsTest().testInvoke();
		String[] parmasArray = { "papam" };
		ReflectionUtils.invokeMethod(ReflectionUtilsTest.class, "ReflectionUtilsTest", parmasArray);

		// new ReflectionUtilsTest().testSetObjectFieldsFromPropertiesFile();
		// testSetStaticFieldsFromProperties();
	}

	public static void setSboolKey1(boolean sboolKey1) {
		ReflectionUtilsTest.sboolKey1 = sboolKey1;
	}

	public static void setSintKey1(int sintKey1) {
		ReflectionUtilsTest.sintKey1 = sintKey1;
	}

	public static void setSkey1(String skey1) {
		ReflectionUtilsTest.skey1 = skey1;
	}

	public static void setSkey2(String skey2) {
		ReflectionUtilsTest.skey2 = skey2;
	}

	public static void setSkey3(String skey3) {
		ReflectionUtilsTest.skey3 = skey3;
	}

	public static String setStaticAttribute(final String stringParam) {
		staticAttribute = stringParam;
		logger.info("staticAttribute: " + staticAttribute);
		return staticAttribute + staticAttribute;
	}

	private static void testSetStaticFieldsFromProperties() {
		ReflectionUtils.setFieldsFromProperties(staticProperties, ReflectionUtilsTest.class);
		logger.info(StringUtils.concat("skey1: ", skey1, "  skey2: ", skey2, "  skey3: ", skey3, "  sintKey1: ",
				String.valueOf(sintKey1), "  sboolKey1: ", String.valueOf(sboolKey1)));
	}

	public int getIintKey1() {
		return iintKey1;
	}

	public String getIkey1() {
		return ikey1;
	}

	public String getIkey2() {
		return ikey2;
	}

	public String getIkey3() {
		return ikey3;
	}

	public boolean isIsboolKey1() {
		return isboolKey1;
	}

	public void setIintKey1(int iintKey1) {
		this.iintKey1 = iintKey1;
	}

	public void setIkey1(String ikey1) {
		this.ikey1 = ikey1;
	}

	public void setIkey2(String ikey2) {
		this.ikey2 = ikey2;
	}

	public void setIkey3(String ikey3) {
		this.ikey3 = ikey3;
	}

	private void setInstanceProperties() {
		instanceProperties.put("ikey1", "ivalue1");
		instanceProperties.put("ikey2", "ivalue2");
		instanceProperties.put("ikey3", "ivalue3");
		instanceProperties.put("iintKey1", "789");
		instanceProperties.put("iboolKey1", "yes");
		instanceProperties.put("remotePropertiesFilePath", "remotePropertiesFilePath9999");
	}

	public void setIsboolKey1(boolean isboolKey1) {
		this.isboolKey1 = isboolKey1;
	}

	public void setRemotePropertiesFilePath(String remotePropertiesFilePath) {
		this.remotePropertiesFilePath = remotePropertiesFilePath;
	}

	private void testInvoke() {
		String className = this.getClass().getCanonicalName();
		logger.info(className);
		String[] parameters = { "str1", "str2", "str3" };
		Object result = ReflectionUtils.invokeMethod(className, "testInvokeMethod3", parameters);
		logger.info(result.toString());
		String[] parameters0 = {};
		result = ReflectionUtils.invokeMethod(className, "testInvokeMethod0", parameters0);
		logger.info(result.toString());
		String[] parameters1 = { "one parameter" };
		result = ReflectionUtils.invokeMethod(className, "testInvokeMethod1", parameters1);
		logger.info(result.toString());
	}

	public String testInvokeMethod0() {
		String result = "****no params****";
		logger.info(result);
		return result;
	}

	public String testInvokeMethod1(final String str1) {
		String result = str1;
		logger.info("****" + result + "****");
		return result;
	}

	public String testInvokeMethod3(final String param1, final String param2, final String param3) {
		String result = param1 + param2 + param3;
		logger.info("****" + result + "****");
		return result;
	}

	private void testSetObjectFieldsFromPropertiesFile() {
		setInstanceProperties();
		ReflectionUtils.setFieldsFromProperties(instanceProperties, this);
		logger.info(StringUtils.concat("ikey1: ", ikey1, " ikey2: ", ikey2, "  ikey3: ", ikey3, "  iintKey1: ",
				String.valueOf(iintKey1), "  iboolKey1: ", String.valueOf(isboolKey1), "  remotePropertiesFilePath: ",
				remotePropertiesFilePath));
	}

}
