package oz.infra.varargs;

public class VarArgsUtils {
	public static <T> T getMyArg(final T defaultArg, final T... args) {
		T myArg = defaultArg;
		if (args.length == 1) {
			myArg = args[0];
		}
		return myArg;
	}
}
