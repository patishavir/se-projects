package oz.temp;

import java.util.logging.Logger;

import oz.infra.logging.jul.JulUtils;

public class TestSwitch {
	private static final Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		String str1 = "aa";
		str1 = "bbb";
		switch (str1) {
		case "aaa":
			System.out.println(str1);
			break;
		case "bbb":
			System.out.println(str1);
			break;
		}
	}

}
