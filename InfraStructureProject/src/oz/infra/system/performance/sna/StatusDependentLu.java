package oz.infra.system.performance.sna;

public class StatusDependentLu {
	private int freeLus;
	private int activeLus;
	private int totalLus;

	public StatusDependentLu(final int freeLus, final int activeLus) {
		this.freeLus = freeLus;
		this.activeLus = activeLus;
		totalLus = activeLus + freeLus;
	}

	public int getActiveLus() {
		return activeLus;
	}

	public int getFreeLus() {
		return freeLus;
	}

	public int getTotalLus() {
		return totalLus;
	}
}
