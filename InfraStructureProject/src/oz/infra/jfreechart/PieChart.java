package oz.infra.jfreechart;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class PieChart {
	private static PieDataset createDataset(final String[][] valuesArray) {
		DefaultPieDataset defaultpiedataset = new DefaultPieDataset();
		for (int rowPtr = 0; rowPtr < valuesArray.length; rowPtr++) {
			double doubleNum = 0.0d;
			if (valuesArray[rowPtr][1] != null && valuesArray[rowPtr][1].length() > 0) {
				doubleNum = Double.parseDouble(valuesArray[rowPtr][1]);
			}
			defaultpiedataset.setValue(valuesArray[rowPtr][0], doubleNum);
		}
		return defaultpiedataset;
	}

	public static JFreeChart createChart(final String chartTitle, final String[][] valuesArray,
			final Color[] colorArray) {
		PieDataset piedataset = createDataset(valuesArray);
		JFreeChart jfreechart = ChartFactory.createPieChart(chartTitle, piedataset, true, true,
				false);
		PiePlot pieplot = (PiePlot) jfreechart.getPlot();
		for (int rowPtr = 0; rowPtr < valuesArray.length; rowPtr++) {
			pieplot.setSectionPaint(valuesArray[rowPtr][0], colorArray[rowPtr]);
		}
		pieplot.setNoDataMessage("No data available");
		pieplot.setExplodePercent("Two", 0.5D);
		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} {1} ({2} percent)"));
		pieplot.setLabelBackgroundPaint(new Color(220, 220, 220));
		return jfreechart;
	}
}
