package oz.infra.io.filesystem;

public class FileSystemDiskUsage {
	private String name;
	private long freeSpaceBytes;
	private long totalSpaceBytes;
	private int precentageUsed;

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final long getFreeSpaceBytes() {
		return freeSpaceBytes;
	}

	public final void setFreeSpaceBytes(final long freeSpaceBytes) {
		this.freeSpaceBytes = freeSpaceBytes;
	}

	public final long getTotalSpaceBytes() {
		return totalSpaceBytes;
	}

	public final void setTotalSpaceBytes(final long totalSpaceBytes) {
		this.totalSpaceBytes = totalSpaceBytes;
	}

	public final int getPrecentageUsed() {
		return precentageUsed;
	}

	public final void setPrecentageUsed(final int precentageUsed) {
		this.precentageUsed = precentageUsed;
	}
}
