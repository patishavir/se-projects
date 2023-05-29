package oz.test.chart;

import org.jfree.chart.JFreeChart;

import oz.infra.jfreechart.StackedBarChart3D;
import oz.infra.swing.SwingUtils;

public class TestChart {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[][] categotyDataSetArray = { { "10", "Series 1", "C1" }, { "5", "Series 1", "C2" },
				{ "6", "Series 1", "C3" }, { "7", "Series 1", "C4" }, { "8", "Series 1", "C5" },
				{ "9", "Series 1", "C6" }, { "10", "Series 1", "C7" }, { "11", "Series 1", "C8" },
				{ "3", "Series 1", "C9" }, { "4", "Series 2", "C1" }, { "7", "Series 2", "C2" },
				{ "17", "Series 2", "C3" }, { "15", "Series 2", "C4" }, { "6", "Series 2", "C5" },
				{ "8", "Series 2", "C6" }, { "9", "Series 2", "C7" }, { "13", "Series 2", "C8" },
				{ "7", "Series 2", "C9" }, { "15", "Series 3", "C1" }, { "88", "Series 3", "C2" },
				{ "12", "Series 3", "C3" }, { "11", "Series 3", "C4" }, { "10", "Series 3", "C5" },
				{ "0.0", "Series 3", "C6" }, { "7", "Series 3", "C7" }, { "9", "Series 3", "C8" },
				{ "11", "Series 3", "C9" }, { "14", "Series 4", "C1" }, { "3", "Series 4", "C2" },
				{ "7", "Series 4", "C3" }, { "0", "Series 4", "C4" }, { "9", "Series 4", "C5" },
				{ "6", "Series 4", "C6" }, { "7", "Series 4", "C7" }, { "9", "Series 4", "C8" },
				{ "10", "Series 4", "C9" } };

		String title = "Title";
		String value = "value";
		String category = "category";
		String orientation = "Horizontal";

		JFreeChart jFreeChart = StackedBarChart3D.createChart(title, category, value, orientation,
				categotyDataSetArray);
		SwingUtils.drawImage(jFreeChart.createBufferedImage(500, 500));
	}
}
