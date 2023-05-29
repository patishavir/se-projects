package jdumper;

import java.util.Vector;

import jdumper.stat.ApplicationProtocolStat;
import jdumper.stat.FreeMemStat;
import jdumper.stat.JDStatisticsTaker;
import jdumper.stat.NetworkProtocolStat;
import jdumper.stat.PacketStat;
import jdumper.stat.TransportProtocolStat;

public class JDStatisticsTakerLoader {
	static Vector stakers = new Vector();

	static void loadStatisticsTaker() {
		stakers.addElement(new PacketStat());
		stakers.addElement(new NetworkProtocolStat());
		stakers.addElement(new TransportProtocolStat());
		stakers.addElement(new ApplicationProtocolStat());
		stakers.addElement(new FreeMemStat());
	}

	public static JDStatisticsTaker[] getStatisticsTakers() {
		JDStatisticsTaker[] array = new JDStatisticsTaker[stakers.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = (JDStatisticsTaker) stakers.elementAt(i);
		return array;
	}

	public static JDStatisticsTaker getStatisticsTakerAt(int index) {
		return (JDStatisticsTaker) stakers.get(index);
	}
}
