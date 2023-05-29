package oz.clearcase.vob;

import oz.infra.string.StringUtils;

public final class VobObject {
	private String vobTag = null;
	private String vobGlobalPath = null;

	public String getVobGlobalPath() {
		return vobGlobalPath;
	}

	public String getVobTag() {
		return vobTag;
	}

	public void setVobGlobalPath(final String vobGlobalPath) {
		this.vobGlobalPath = vobGlobalPath;
	}

	public void setVobTag(final String vobTag) {
		this.vobTag = vobTag;
	}

	public String toString() {
		String str = StringUtils.concat("vobTag: ", vobTag, " vobGlobalPath: ", vobGlobalPath);
		return str;
	}

}
