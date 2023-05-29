package oz.clearcase.view.ccviewsmgr.viewoperations;

import oz.clearcase.view.ccviewsmgr.CCViewsParameters;

public class RunScriptOperation implements IViewOperation {
	public boolean exec(final EViewOperation viewOperation, final String[] params) {
		String[] clearToolparams = { "call", CCViewsParameters.getScript2Run(), params[0],
				params[1] };
		MultipleViewsOperation.add2Script(clearToolparams);
		return true;
	}
}
