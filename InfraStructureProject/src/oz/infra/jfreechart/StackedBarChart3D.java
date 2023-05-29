package oz.infra.jfreechart;

import java.util.logging.Logger;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

public class StackedBarChart3D {
	private static Logger logger = Logger.getLogger(StackedBarChart3D.class.getName());

	private static CategoryDataset createDataset(final String[][] categorydatasetArray) {
		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		for (int i = 0; i < categorydatasetArray.length; i++) {
			defaultcategorydataset.addValue(Double.parseDouble(categorydatasetArray[i][0]),
					categorydatasetArray[i][1], categorydatasetArray[i][2]);
		}
		return defaultcategorydataset;
	}

	public static final JFreeChart createChart(final String title, final String catergory,
			final String value, final String orientation, final String[][] categorydatasetArray) {
		logger.info("Starting create chart");
		CategoryDataset categoryDataset = createDataset(categorydatasetArray);
		PlotOrientation plotOrientation = PlotOrientation.VERTICAL;
		if (orientation.equalsIgnoreCase("HORIZONTAL")) {
			plotOrientation = PlotOrientation.HORIZONTAL;
		}
		JFreeChart jfreechart = ChartFactory.createStackedBarChart3D(title, catergory, value,
				categoryDataset, plotOrientation, true, true, false);
		CategoryPlot categoryplot = (CategoryPlot) jfreechart.getPlot();
		BarRenderer barrenderer = (BarRenderer) categoryplot.getRenderer();
		barrenderer.setDrawBarOutline(false);
		barrenderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		barrenderer.setItemLabelsVisible(true);
		barrenderer.setPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER,
				TextAnchor.CENTER));
		barrenderer.setNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER,
				TextAnchor.CENTER));
		return jfreechart;
	}
}
