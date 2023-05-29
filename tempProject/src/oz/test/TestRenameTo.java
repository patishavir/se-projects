package oz.test;

import java.io.File;

public class TestRenameTo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File in = new File("c:\\temp\\error.log");
		// File out = new File("o:\\temp$\\oz.txt");
		File out = new File("c:\\temp\\error.log.Z");
		System.out.println(String.valueOf(in.exists()));

		boolean rc = in.renameTo(out);
		System.out.println(String.valueOf(rc));

	}
}
