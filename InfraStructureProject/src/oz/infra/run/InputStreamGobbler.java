package oz.infra.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

class InputStreamGobbler extends Thread {
	private InputStream inputStream;
	private InputStreamReader inputStreamReader;
	private BufferedReader bufferedReader;
	private StringBuffer streamBuffer = new StringBuffer();
	private RunExec runExec = null;
	private boolean readCompleted = false;
	private static final int SLEEP_INTERVAL = 100;
	private final String streamId;
	private static final int BUFFER_SIZE = 2000;
	private Logger logger = Logger.getLogger(this.getClass().toString());

	InputStreamGobbler(final RunExec runExecP, final InputStream is, final String streamIdP) {
		this.inputStream = is;
		streamBuffer.append("");
		inputStreamReader = new InputStreamReader(inputStream);
		bufferedReader = new BufferedReader(inputStreamReader, BUFFER_SIZE);
		runExec = runExecP;
		streamId = streamIdP;
	}

	public void run() {
		do {
			readLines();
			if (runExec.isProcessCompleted()) {
				break;
			}
			try {
				sleep(SLEEP_INTERVAL);
				logger.finest(streamId + ": Sleep for " + String.valueOf(SLEEP_INTERVAL)
						+ " milli seconds");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} while (!runExec.isProcessCompleted());
		readLines();
		readCompleted = true;
	}

	/*
	 * readlines
	 */
	void readLines() {
		String line = null;
		logger.finest("Starting readLines for " + streamId);
		try {
			while ((line = bufferedReader.readLine()) != null) {
				if (streamBuffer.length() != 0) {
					streamBuffer.append("\n");
				}
				logger.finest(line + "        " + String.valueOf(line.length()));
				streamBuffer.append(line);
			}
			logger.finest("Available bytes for " + streamId + ":"
					+ String.valueOf(inputStream.available()));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		logger.finer("Exiting from readLines. Stream id: " + streamId);
	}

	/*
	 * getStreamBuffer()
	 */
	String getStreamBuffer() {
		while (!readCompleted) {
			try {
				sleep(SLEEP_INTERVAL);
				logger.finest(streamId + ": Sleep for " + String.valueOf(SLEEP_INTERVAL)
						+ " milli seconds");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return streamBuffer.toString();
	}
}