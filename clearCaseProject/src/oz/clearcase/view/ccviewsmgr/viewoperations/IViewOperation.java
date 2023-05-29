package oz.clearcase.view.ccviewsmgr.viewoperations;

public interface IViewOperation {
	boolean exec(final EViewOperation viewOperation, final String[] params);
}
