package oz.temp.sshj;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;

/** This examples demonstrates how a remote command can be executed. */
public class Test {

    public static void main(String... args)
            throws IOException {
        final SSHClient ssh = new SSHClient();

        try {
            File f = new File("C:/Mataf/ssh/known_hosts");
            System.out.println(f.exists() + " "+f.isFile());
            ssh.loadKnownHosts(f);
            ssh.connect("10.18.188.115");
            ssh.authPublickey("root","C:/Mataf/ssh/prv.ppk");
            final Session session = ssh.startSession();
            try {
                final Command cmd = session.exec("ls -l /tmp");
                System.out.println(IOUtils.readFully(cmd.getInputStream()).toString());
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
