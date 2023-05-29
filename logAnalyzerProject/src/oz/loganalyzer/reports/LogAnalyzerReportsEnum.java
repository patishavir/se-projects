package oz.loganalyzer.reports;

import oz.loganalyzer.reports.obsolete.ApplicationServerReport;
import oz.loganalyzer.reports.obsolete.BranchActivityReport;
import oz.loganalyzer.reports.obsolete.EnvironmentReport;

;

public enum LogAnalyzerReportsEnum {
	ResponseTimeReport(new ResponseTimeReport()), ApplicationServerReport(new ApplicationServerReport()), ApplicationServerSummaryReport(
			new ApplicationServerSummaryReport()), EnvironmentReport(new EnvironmentReport()), EnvironmentSummaryReport(
			new EnvironmentSummaryReport()), BranchActivityReport(new BranchActivityReport()), BranchSummaryReport(
			new BranchSummaryReport()), SlowTransactionsReport(new SlowTransactionsReport()), TransactionTypeSummaryReport(
			new TransactionTypeSummaryReport()), UserLoginReport(new UserLoginReport()), NumberOfUserLoginsReport(
			new NumberOfUserLoginsReport()), ApplicationSummaryReport(new ApplicationSummaryReport()), LongResponseReport(
			new LongResponseReport()), StatusCodeSummaryReport(new StatusCodeSummaryReport()), TopUsersSummaryReport(
			new TopUsersSummaryReport()), ByMinuteReport(new ByMinuteReport()), DistinctUsersReport(new DistinctUsersReport());
	private AbstractLogAnalyzerReport reportObject;

	public AbstractLogAnalyzerReport getReportObject() {
		return reportObject;
	}

	LogAnalyzerReportsEnum(final AbstractLogAnalyzerReport abstractLogAnalyzerReport) {
		reportObject = abstractLogAnalyzerReport;
	}
}
