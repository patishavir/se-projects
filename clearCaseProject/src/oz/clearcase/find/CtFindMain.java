package oz.clearcase.find;

import oz.infra.parameters.XMLFileParameters;

public class CtFindMain {
	private static boolean check4InputParameters = false;

	public static void main(final String[] args) {
		if (args.length == 1) {
			check4InputParameters = XMLFileParameters.buildInputParameters(args[0]) == 1;
		}
		new CtFindJFrame();
	}

	static boolean isCheck4InputParameters() {
		return check4InputParameters;
	}
}