package oz.infra.runtime.test;

import java.util.logging.Level;

import oz.infra.runtime.RunTimeUtils;

public class RunTimeUtilsTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RunTimeUtils.displayMemoryInfo(Level.INFO);
		RunTimeUtils.gc(Level.INFO);
	}

}
