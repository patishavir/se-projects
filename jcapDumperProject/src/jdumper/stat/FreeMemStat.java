package jdumper.stat;

import java.util.Vector;

public class FreeMemStat extends JDStatisticsTaker {
	String[] labels = { "Free Memory" };
	String[] types = { "Bytes" };

	public String getName() {
		return "Free Memory";
	}

	public void analyze(Vector packets) {
	}

	public void addPacket(jpcap.packet.Packet p) {
	}

	public String[] getLabels() {
		return labels;
	}

	public String[] getStatTypes() {
		return types;
	}

	public long[] getValues(int index) {
		long[] ret = new long[1];
		ret[0] = Runtime.getRuntime().freeMemory();
		return ret;
	}

	public void clear() {
	}
}
