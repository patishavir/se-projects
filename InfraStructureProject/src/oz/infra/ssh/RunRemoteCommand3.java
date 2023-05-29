/******************************************************************************
 *
 * Copyright (c) 1999-2005 AppGate Network Security AB. All Rights Reserved.
 * 
 * This file contains Original Code and/or Modifications of Original Code as
 * defined in and that are subject to the MindTerm Public Source License,
 * Version 2.0, (the 'License'). You may not use this file except in compliance
 * with the License.
 * 
 * You should have received a copy of the MindTerm Public Source License
 * along with this software; see the file LICENSE.  If not, write to
 * AppGate Network Security AB, Otterhallegatan 2, SE-41118 Goteborg, SWEDEN
 *
 *****************************************************************************/

package oz.infra.ssh;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

import com.mindbright.jca.security.SecureRandom;
import com.mindbright.ssh2.SSH2ConsoleRemote;
import com.mindbright.ssh2.SSH2Preferences;
import com.mindbright.ssh2.SSH2SimpleClient;
import com.mindbright.ssh2.SSH2Transport;
import com.mindbright.util.RandomSeed;
import com.mindbright.util.SecureRandomAndPad;
import com.mindbright.util.Util;

import examples.RemoteShellScript;
import examples.RunRemoteCommand;
import oz.infra.logging.jul.JulUtils;
import oz.infra.run.processbuilder.SystemCommandExecutorResponse;
import oz.infra.system.SystemUtils;

/**
 * This is a variant of RunRemoteCommand which processes the stdio streams
 * slightly differently, otherwise it does exactly the same thing.
 * <p>
 * Usage: <code> java -cp examples.jar examples.RunRemoteCommand2
 * <em>server</em>[:<em>port</em>] <em>username</em> <em>password</em>
 * <em>command_line</em>
 * 
 * @see RemoteShellScript
 * @see RunRemoteCommand
 */
public class RunRemoteCommand3 {
	private static Logger logger = JulUtils.getLogger();

	/**
	 * Run the application
	 */
	public static SystemCommandExecutorResponse runRemoteCommand(String[] argv) {
		int exitStatus = -1;
		StringBuilder stdoutSb = null;
		StringBuilder stderrSb = null;
		if (argv.length < 4) {
			logger.warning("usage: RunRemoteCommand2 <server[:port]> <username> <password> <command-line>");
			return new SystemCommandExecutorResponse(1, null, null);
		}
		try {
			String server = argv[0];
			String user = argv[1];
			String passwd = argv[2];
			int port = 22;

			port = Util.getPort(server, 22);
			server = Util.getHost(server);

			String cmdLine = "";
			for (int i = 3; i < argv.length; i++) {
				cmdLine += argv[i] + " ";
			}
			cmdLine = cmdLine.trim();

			SSH2Preferences prefs = new SSH2Preferences();

			/*
			 * Set some preferences, i.e. force usage of blowfish cipher and set
			 * loglevel to 6 (debug) and log output to the file ssh2out.log
			 */
			prefs.setPreference(SSH2Preferences.CIPHERS_C2S, "blowfish-cbc");
			prefs.setPreference(SSH2Preferences.CIPHERS_S2C, "blowfish-cbc");
			// prefs.setPreference(SSH2Preferences.LOG_LEVEL, "6");
			prefs.setPreference(SSH2Preferences.LOG_LEVEL, "1");
			prefs.setPreference(SSH2Preferences.LOG_FILE, "/tmp/ssh2out.log");

			/*
			 * Connect to the server and authenticate using plain password
			 * authentication (if other authentication method needed check other
			 * constructors for SSH2SimpleClient).
			 */
			Socket serverSocket = new Socket(server, port);
			SSH2Transport transport = new SSH2Transport(serverSocket, prefs, createSecureRandom());
			SSH2SimpleClient client = new SSH2SimpleClient(transport, user, passwd);

			/*
			 * Create the remote console to use for command execution. Here we
			 * redirect stderr of all sessions started with this console to our
			 * own stderr (NOTE: stdout is NOT redirected here but is instead
			 * fetched below).
			 */
			SSH2ConsoleRemote console = new SSH2ConsoleRemote(client.getConnection(), null, System.err);

			/*
			 * Run the command. Here we don't redirect stdout and stderr but use
			 * the internal streams of the session channel instead.
			 */
			if (console.command(cmdLine)) {
				/*
				 * Fetch the internal stdout stream and wrap it in a
				 * BufferedReader for convenience.
				 */
				BufferedReader stdout = new BufferedReader(new InputStreamReader(console.getStdOut()));

				/*
				 * Read all output sent to stdout (line by line) and print it to
				 * our own stdout.
				 */
				String line;
				stdoutSb = new StringBuilder();
				while ((line = stdout.readLine()) != null) {
					stdoutSb.append(line);
					stdoutSb.append(SystemUtils.LINE_SEPARATOR);
				}
				System.out.println(stdoutSb.toString());
				/*
				 * Retrieve the exit status of the command (from the remote
				 * end).
				 */
				exitStatus = console.waitForExitStatus();
			} else {
				System.err.println("failed to execute command: " + cmdLine);
			}

			/*
			 * Disconnect the transport layer gracefully
			 */
			client.getTransport().normalDisconnect("User disconnects");

			/*
			 * Exit with same status as remote command did
			 */
			return (new SystemCommandExecutorResponse(exitStatus, stdoutSb.toString(), null));

		} catch (Exception ex) {
			System.err.println("An error occured: " + ex);
			ex.printStackTrace();
			return (new SystemCommandExecutorResponse(exitStatus, null, null));
		}
	}

	/**
	 * Create a random number generator. This implementation uses the system
	 * random device if available to generate good random numbers. Otherwise it
	 * falls back to some low-entropy garbage.
	 */
	private static SecureRandomAndPad createSecureRandom() {
		byte[] seed;
		File devRandom = new File("/dev/urandom");
		if (devRandom.exists()) {
			RandomSeed rs = new RandomSeed("/dev/urandom", "/dev/urandom");
			seed = rs.getBytesBlocking(20);
		} else {
			seed = RandomSeed.getSystemStateHash();
		}
		return new SecureRandomAndPad(new SecureRandom(seed));
	}
}
