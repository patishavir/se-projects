/*
 * Created on 24/07/2005
 */
package oz.perfmon;

import oz.infra.parameters.InputParameters;

/**
 * @author Oded
 */
public final class PerfMonParameters {
	private static String command1 = null;
	private static String command10 = null;
	private static String command2 = null;
	private static String command3 = null;
	private static String command4 = null;
	private static String command5 = null;
	private static String command6 = null;
	private static String command7 = null;
	private static String command8 = null;
	private static String command9 = null;
	private static String numberOfCommands = "10";

	public static String getCommand1() {
		return command1;
	}

	public static String getCommand10() {
		return command10;
	}

	public static String getCommand2() {
		return command2;
	}

	public static String getCommand3() {
		return command3;
	}

	public static String getCommand4() {
		return command4;
	}

	public static String getCommand5() {
		return command5;
	}

	public static String getCommand6() {
		return command6;
	}

	public static String getCommand7() {
		return command7;
	}

	public static String getCommand8() {
		return command8;
	}

	public static String getCommand9() {
		return command9;
	}

	public static String getNumberOfCommands() {
		return numberOfCommands;
	}

	public static void setCommand1(final String command1) {
		PerfMonParameters.command1 = command1;
	}

	public static void setCommand10(final String command10) {
		PerfMonParameters.command10 = command10;
	}

	public static void setCommand2(final String command2) {
		PerfMonParameters.command2 = command2;
	}

	public static void setCommand3(final String command3) {
		PerfMonParameters.command3 = command3;
	}

	public static void setCommand4(final String command4) {
		PerfMonParameters.command4 = command4;
	}

	public static void setCommand5(final String command5) {
		PerfMonParameters.command5 = command5;
	}

	public static void setCommand6(final String command6) {
		PerfMonParameters.command6 = command6;
	}

	public static void setCommand7(final String command7) {
		PerfMonParameters.command7 = command7;
	}

	public static void setCommand8(final String command8) {
		PerfMonParameters.command8 = command8;
	}

	public static void setCommand9(final String command9) {
		PerfMonParameters.command9 = command9;
	}

	public static void setNumberOfCommands(final String numberOfCommands) {
		PerfMonParameters.numberOfCommands = numberOfCommands;
	}

	/*
	 * ProcessInputParameters method
	 */
	void processParameters(final String[] args) {
		final String[][] parametersArray = { { "command1", null }, { "command2", null }, { "command3", "yes" },
				{ "command4", null }, { "command5", null }, { "command6", null }, { "command7", null },
				{ "command8", null }, { "command9", null }, { "command10", null } };
		new InputParameters().processParameters(args, parametersArray, this);
		// complete parameters processing
	}
}