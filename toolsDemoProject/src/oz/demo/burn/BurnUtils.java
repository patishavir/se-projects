package oz.demo.burn;

import java.util.ArrayList;

public class BurnUtils {

	public static String burnCpu(String str1, int iterations) {
		String str = "";
		try {
			str = "*";
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		for (int i = 0; i < iterations; i++) {
			str = str + str1;
		}
		return str;
	}

	public static ArrayList<BurnUtils> burnMemory(int iterations) {
		ArrayList<BurnUtils> ar = new ArrayList<BurnUtils>();
		for (int i = 0; i < iterations; i++) {
			ar.add(new BurnUtils());
		}
		return ar;
	}
}
