package oz.clearcase.view.ccviewsmgr.viewoperations;

public class RenameViewTagOperation implements IViewOperation {
	public boolean exec(final EViewOperation viewOperation, final String[] params) {

		String newViewTag = params[2];
		if (newViewTag != null) {
			boolean RC = EViewOperation.RemoveViewTagOperation.getOperationClassObject().exec(
					EViewOperation.RemoveViewTagOperation, params);
			if (!RC) {
				return false;
			}
			RC = EViewOperation.UnregisterViewOperation.getOperationClassObject().exec(
					EViewOperation.UnregisterViewOperation, params);
			if (!RC) {
				return false;
			}
			String[] newParams = { newViewTag, params[1] };
			RC = EViewOperation.RegisterViewOperation.getOperationClassObject().exec(
					EViewOperation.RegisterViewOperation, newParams);
			if (!RC) {
				return false;
			}
			RC = EViewOperation.MakeViewTagOperation.getOperationClassObject().exec(
					EViewOperation.MakeViewTagOperation, newParams);
			if (!RC) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
}
