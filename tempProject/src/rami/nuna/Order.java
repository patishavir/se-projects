package rami.nuna;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Order {

	private static int kebabCost = 12;
	private static int pargitCost = 20;
	private static int kruvCost = 22;
	private static int yerakotCost = 12;

	static void processOrder(final Parameters parameters) {

		int totalKebab = 0;
		int totalPargit = 0;
		int totalKruv = 0;
		int totalYerakot = 0;

		if (parameters.getMen() > 0) {
			totalKebab += 200;
			totalPargit += 500;
		}
		if (parameters.getWomen() > 0) {
			totalKebab += 400;
		}
		if (parameters.getKids() > 0) {
			totalKebab += 100;
			totalPargit += 100;
		}
		if (parameters.getVegi() > 0) {
			totalKruv += 1;
			totalYerakot += 1;
		}
		double totalCost = (totalKebab / 100 * kebabCost) + (totalPargit / 100 * pargitCost)
				+ (totalKruv * kruvCost) + (totalYerakot * yerakotCost);

		if (parameters.isStudent()) {
			totalCost = totalCost * 0.95;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("\n" + parameters.getName() + "\n");
		if (totalKebab > 0) {
			sb.append("\n");
			sb.append(String.valueOf(totalKebab));
			sb.append("  גרם קבב  ");
		}
		if (totalPargit > 0) {
			sb.append("\n");
			sb.append(String.valueOf(totalPargit));
			sb.append("  גרם פרגיות  ");
		}
		if (totalKruv > 0) {
			sb.append("\n");
			sb.append(String.valueOf(totalKruv));
			sb.append("  שקיות  כרוב  ");
		}
		if (totalYerakot > 0) {
			sb.append("\n");
			sb.append(String.valueOf(totalYerakot));
			sb.append("  שקיות  ירקות  ");
		}

		sb.append("\n");
		sb.append("\n\n " + new DecimalFormat("###0.00").format(totalCost) + "  סכום לתשלום  ");
		sb.append("\n\n " + "! תודה רבה ולהתראות  ");

		String title = "חשבון";
		JTextArea jtextArea = new JTextArea(sb.toString());
		jtextArea.setAlignmentX(Component.RIGHT_ALIGNMENT);
		jtextArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		JOptionPane.showMessageDialog(null, jtextArea, title, JOptionPane.PLAIN_MESSAGE);
		System.exit(0);

	}
}
