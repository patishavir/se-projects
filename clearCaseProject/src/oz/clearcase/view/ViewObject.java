package oz.clearcase.view;

public class ViewObject {
	private String viewTag = null;
	private String viewStragePath = null;

	public String getViewStragePath() {
		return viewStragePath;
	}

	public String getViewTag() {
		return viewTag;
	}

	public void setViewStragePath(String viewStragePath) {
		this.viewStragePath = viewStragePath;
	}

	public void setViewTag(String viewTag) {
		this.viewTag = viewTag;
	}
}
