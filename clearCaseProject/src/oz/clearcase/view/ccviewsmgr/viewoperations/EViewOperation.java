package oz.clearcase.view.ccviewsmgr.viewoperations;

public enum EViewOperation {
	LsViewOperation("List selected views", new CleartoolViewOperation(), false, "lsview,-l,-prop",
			0), RegenViewDotDatOperation("Regen view.dat+update selected views",
			new RegenViewDotDatOperation(), false, null, 0), RemoveViewOperation(
			"Remove selected views", new CleartoolViewOperation(), false, "rmview,-force", 1), StartViewOperation(
			"Start selected views", new CleartoolViewOperation(), false, "startview", 0), EndViewOperation(
			"End selected views", new CleartoolViewOperation(), false, "endview,-server", 0), RemoveViewTagOperation(
			"Remove selected view tags", new CleartoolViewOperation(), true, "rmtag,-view", 0), RegisterViewOperation(
			"Register selected views", new CleartoolViewOperation(), true, "register,-view", 1), UnregisterViewOperation(
			"Unregister selected views", new CleartoolViewOperation(), true, "unregister,-view", 1), RenameViewTagOperation(
			"Rename view tag for selected views", new RenameViewTagOperation(), true, null, 0), RemoveActivityFromViewOperation(
			"Remove activity from selected views", new CleartoolViewOperation(), true,
			"setactivity,-view", 0), RunScriptOperation("Run Script for selected views",
			new RunScriptOperation(), true, null, 0),
	//
	MakeViewTagOperation("Make view tag", new CleartoolViewOperation(), true, "mktag,-view,-tag",
			-1);
	private String description;

	private IViewOperation operationClassObject;

	private boolean admininstratorOeration = false;

	private String[] operationCleartoolParametersArray = null;

	private int paramsIndex;

	EViewOperation(final String description, final IViewOperation operationClassObject,
			final boolean admininstratorOeration, final String parametersString,
			final int paramsIndex) {
		this.description = description;
		this.operationClassObject = operationClassObject;
		this.admininstratorOeration = admininstratorOeration;
		this.paramsIndex = paramsIndex;
		if (parametersString != null) {
			operationCleartoolParametersArray = parametersString.split(",");
		}
	}

	public final boolean isAdmininstratorOeration() {
		return admininstratorOeration;
	}

	public final String getDescription() {
		return description;
	}

	public final IViewOperation getOperationClassObject() {
		return operationClassObject;
	}

	public final String[] getOperationCleartoolParametersArray() {
		return operationCleartoolParametersArray;
	}

	public final int getParamsIndex() {
		return this.paramsIndex;
	}
}
