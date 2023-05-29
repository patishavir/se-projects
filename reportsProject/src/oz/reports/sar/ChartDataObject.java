package oz.reports.sar;

public class ChartDataObject {
	private String[][] chartDataArray;
	private String chartLabel;

	public final String[][] getChartDataArray() {
		return chartDataArray;
	}

	public final void setChartDataArray(String[][] chartDataArray) {
		this.chartDataArray = chartDataArray;
	}

	public final String getChartLabel() {
		return chartLabel;
	}

	public final void setChartLabel(String chartLabel) {
		this.chartLabel = chartLabel;
	}
}
