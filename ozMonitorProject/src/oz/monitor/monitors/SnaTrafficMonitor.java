package oz.monitor.monitors;

import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import oz.infra.constants.OzConstants;
import oz.infra.reflection.ReflectionUtils;
import oz.infra.system.performance.PerformanceUtils;
import oz.infra.thread.ThreadUtils;
import oz.monitor.common.OzMonitorResponse;

public class SnaTrafficMonitor extends AbstractOzMonitor {
	private static final int INITIAL_INTERVAL = 60;
	// name = ifs
	// stats_type = LS
	// table_type = STATS
	// dlc_type = ETHERNET
	// ls_st_mus_sent = 291
	// ls_st_mus_received = 329
	// ls_st_bytes_sent = 16144
	// ls_st_bytes_received = 71173
	// ls_st_test_cmds_sent = 0
	// ls_st_test_cmds_rec = 0
	// ls_st_data_pkt_resent = 0
	// ls_st_inv_pkt_rec = 0
	// ls_st_adp_rec_err = 0
	// ls_st_adp_send_err = 0
	// ls_st_rec_inact_to = 0
	// ls_st_cmd_polls_sent = 444
	// ls_st_cmd_repolls_sent = 0
	// ls_st_cmd_cont_repolls = 0
	private long bytesSent = 0;
	private long bytesReceived = 0;
	private long lastBytesSent = 0;
	private long lastBytesReceived = 0;
	private long lastTimeStamp = 0;
	private String bytesSentString = null;
	private String bytesReceivedString = null;

	public SnaTrafficMonitor(final Properties monitorProperties) {
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this);
		ReflectionUtils.setFieldsFromProperties(monitorProperties, this, this.getClass().getSuperclass());
	}

	private long getByteCount(final long currentFigure, final long lastFigure) {
		long byteCount = currentFigure - lastFigure;
		if (byteCount < 0) {
			byteCount = currentFigure + (long) (Math.pow(2.0, 32.0)) - lastFigure;
		}
		return byteCount;
	}

	private void getSnaTrafficSatistics() {
		Map<String, String> snaTrafficMap = PerformanceUtils.getSnaTrafficStatistics();
		bytesSentString = "0";
		bytesReceivedString = "0";
		if (snaTrafficMap != null) {
			bytesSentString = snaTrafficMap.get("ls_st_bytes_sent");
			bytesReceivedString = snaTrafficMap.get("ls_st_bytes_received");
			logger.finest(OzConstants.NUMBER_SIGN + "bytesSentString:" + bytesSentString + OzConstants.NUMBER_SIGN
					+ "bytesReceivedString:" + bytesReceivedString + OzConstants.NUMBER_SIGN);
			bytesSent = Long.parseLong(bytesSentString);
			bytesReceived = Long.parseLong(bytesReceivedString);
		}
	}

	public final OzMonitorResponse isResourceStatusOK(final OzMonitorDispatcher ozMonitorDispatcher) {
		logger.info("Monitoring: " + getResource());
		getSnaTrafficSatistics();
		if ((lastBytesReceived + lastBytesSent) == 0) {
			lastBytesReceived = bytesReceived;
			lastBytesSent = bytesSent;
			lastTimeStamp = System.currentTimeMillis() / OzConstants.INT_1000;
			ThreadUtils.sleep(INITIAL_INTERVAL * OzConstants.INT_1000, Level.INFO);
			getSnaTrafficSatistics();
		}
		long currentTime = System.currentTimeMillis() / OzConstants.INT_1000;
		long actualInterval = currentTime - lastTimeStamp;
		long bytesSentInPerSecond = getByteCount(bytesSent, lastBytesSent) / actualInterval;
		long bytesReceivedInPerSecond = getByteCount(bytesReceived, lastBytesReceived) / actualInterval;
		StringBuilder ozMonitorMessageSB = new StringBuilder();
		ozMonitorMessageSB.append("SNA Bps sent: ");
		ozMonitorMessageSB.append(String.valueOf(bytesSentInPerSecond));
		ozMonitorMessageSB.append(" received: ");
		ozMonitorMessageSB.append(String.valueOf(bytesReceivedInPerSecond));
		ozMonitorMessageSB.append(" Total sent: ");
		ozMonitorMessageSB.append(bytesSentString);
		ozMonitorMessageSB.append(" received: ");
		ozMonitorMessageSB.append(bytesReceivedString);
		String ozMonitorMessage = ozMonitorMessageSB.toString();

		boolean isResourceAvailable = ((bytesSentInPerSecond + bytesSentInPerSecond) > 0);

		lastBytesReceived = bytesReceived;
		lastBytesSent = bytesSent;
		lastTimeStamp = currentTime;

		return generateOzMonitorResponse(isResourceAvailable, ozMonitorMessage, null);
	}
}
