package oz.infra.datetime;

public class TimePeriod {
	private long startTime;
	private long endTime;
	private long duration;

	//
	public TimePeriod(final long startTime) {
		this.startTime = startTime;
	}

	public final long getDuration() {
		return this.duration;
	}

	public final long getEndTime() {
		return this.endTime;
	}

	public final long getStartTime() {
		return this.startTime;
	}

	public final void setDuration(final long duration) {
		this.duration = duration;
	}

	public final void setEndTime(final long endTime) {
		this.endTime = endTime;
	}

	public final void setStartTime(final long startTime) {
		this.startTime = startTime;
	}
}
