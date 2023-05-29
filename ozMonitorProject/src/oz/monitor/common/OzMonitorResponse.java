package oz.monitor.common;

public class OzMonitorResponse {
	private boolean resourceStatus = false;
	private String ozMonitorMessage = null;
	private String ozMonitorLongMessage = null;
	private boolean performOnFailureAction = true;
	private boolean performOnSuccessAction = true;
	private boolean performSendAlerts = true;
	private String[] actionScriptParameters = null;

	public OzMonitorResponse(final boolean resourceStatus, final String ozMonitorMessage,
			final String ozMonitorLongMessage) {
		this.resourceStatus = resourceStatus;
		this.ozMonitorMessage = ozMonitorMessage;
		this.ozMonitorLongMessage = ozMonitorLongMessage;
	}

	public final String[] getActionScriptParameters() {
		return actionScriptParameters;
	}

	public final String getOzMonitorLongMessage() {
		return ozMonitorLongMessage;
	}

	public final String getOzMonitorMessage() {
		return ozMonitorMessage;
	}

	public final boolean isPerformOnFailureAction() {
		return performOnFailureAction;
	}

	public final boolean isPerformOnSuccessAction() {
		return performOnSuccessAction;
	}

	public final boolean isPerformSendAlerts() {
		return performSendAlerts;
	}

	public final boolean isResourceStatus() {
		return resourceStatus;
	}

	public final void setActionScriptParameters(final String[] actionScriptParameters) {
		this.actionScriptParameters = actionScriptParameters;
	}

	public final void setOzMonitorLongMessage(final String ozMonitorLongMessage) {
		this.ozMonitorLongMessage = ozMonitorLongMessage;
	}

	public final void setOzMonitorMessage(final String ozMonitorMessage) {
		this.ozMonitorMessage = ozMonitorMessage;
	}

	public final void setPerformOnFailureAction(final boolean performOnFailureAction) {
		this.performOnFailureAction = performOnFailureAction;
	}

	public final void setPerformOnSuccessAction(final boolean performOnSuccessAction) {
		this.performOnSuccessAction = performOnSuccessAction;
	}

	public final void setPerformSendAlerts(final boolean performSendAlerts) {
		this.performSendAlerts = performSendAlerts;
	}

	public final void setResourceStatus(final boolean resourceStatus) {
		this.resourceStatus = resourceStatus;
	}

}
