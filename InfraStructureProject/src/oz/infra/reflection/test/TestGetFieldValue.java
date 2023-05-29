package oz.infra.reflection.test;

import oz.infra.reflection.ReflectionUtils;

public class TestGetFieldValue {
	private int int1 = 17;
	private String string1 = "177str+++";
	private long long1 = 889900L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestGetFieldValue testGetFieldValue = new TestGetFieldValue();
		testGetFieldValue.runTest();

	}

	void runTest() {
		ReflectionUtils.getFieldValueAsString(this, "int1");
		ReflectionUtils.getFieldValueAsString(this, "string1");
		ReflectionUtils.getFieldValueAsString(this, "long1");
	}

	public int getInt1() {
		return int1;
	}

	public String getString1() {
		return string1;
	}

	public long getLong1() {
		return long1;
	}
}
