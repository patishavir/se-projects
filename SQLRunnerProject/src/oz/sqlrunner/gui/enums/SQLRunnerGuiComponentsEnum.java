package oz.sqlrunner.gui.enums;

public enum SQLRunnerGuiComponentsEnum {
	About(), Connect("Connect to DB"), DisConnect("Disconnect from DB"), Filter("Filter DB objects"), Run(
			"Run SQL statement"), NewSqlStatement("New SQL statement"), Exit(), Options(), Refresh(), Usage();
	private String displayName = null;

	SQLRunnerGuiComponentsEnum() {
		this.displayName = this.toString();
	}

	SQLRunnerGuiComponentsEnum(final String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
