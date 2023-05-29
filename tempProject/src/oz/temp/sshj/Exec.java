/**
 * Copyright 2009 sshj contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package oz.temp.sshj;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;

/** This examples demonstrates how a remote command can be executed. */
public class Exec {

	public static void main(String... args) throws IOException {
		final SSHClient ssh = new SSHClient();
		// ssh.loadKnownHosts();
		 ssh.addHostKeyVerifier("0f:b4:0e:39:1a:7a:18:79:1f:b3:11:bc:e2:a8:ec:c9");
		ssh.connect("suswastest2");
		try {
			ssh.authPublickey("root");
			final Session session = ssh.startSession();
			try {
				final Command cmd = session.exec("cal");
				String stringgg = IOUtils.readFully(cmd.getInputStream()).toString();
				System.out.println("++++++++++++++++++++++++++\n" + stringgg);
				cmd.join(5, TimeUnit.SECONDS);
				System.out.println("\n** exit status: " + cmd.getExitStatus());
			} finally {
				session.close();
			}
		} finally {
			ssh.disconnect();
		}
	}

}
