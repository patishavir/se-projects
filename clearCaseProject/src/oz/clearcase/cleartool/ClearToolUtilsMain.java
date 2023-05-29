package oz.clearcase.cleartool;

public class ClearToolUtilsMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// ClearToolUtils.getVobsInfoList();
		// ClearToolUtils.generateMoveVobsScript();
		// ClearToolUtils.generateMoveViewsScript();
		ClearToolUtils.generateRemoveUnuseedViewsScript("l:\\temp$\\lsview-l-prop.txt1", 2016, "l:\\temp$\\lsview-l-prop.bat");
		// ClearToolUtils.generateRemoveViewsScript("args/noexistingUsers.txt");
	}

}
