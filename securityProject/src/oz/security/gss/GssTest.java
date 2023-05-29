package oz.security.gss;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

import oz.infra.logging.jul.JulUtils;
import sun.net.www.protocol.http.HttpURLConnection;
import sun.security.jgss.GSSUtil;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class GssTest extends JFrame {
	private static final long serialVersionUID = 571793660787774890L;
	private static Logger logger = JulUtils.getLogger();

	public static void main(final String[] args) throws Exception {
		String argsPath = ".\\args\\gsstest\\";
		File argsFile = new File(argsPath);
		logger.info(argsFile.getAbsolutePath());
		System.setProperty("java.security.krb5.conf", argsPath + "spnego-krb5.conf");
		System.setProperty("java.security.auth.login.config", argsPath + "spnego-jaas.conf");
		System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
		System.setProperty("java.security.debug", "all");
		System.setProperty("http.auth.preference", "spnego");
		// SystemUtils.printSystemProperties();
		String server = "snif-http.fibi.corp";
		if (CookieManager.getDefault() == null) {
			CookieManager.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		}
		// URL url = new URL("http://www.fibi-kidma.co.il/snoop");
		URL url = new URL("http://" + server +"/snoop");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		InputStream is = null;
		try {
			is = conn.getInputStream();
		} catch (Exception e1) {
			logger.warning(e1.getMessage());
			if (conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
				String negotialte = conn.getHeaderField("WWW-Authenticate");
				if (negotialte.startsWith("Negotiate")) {
					Oid spnegoMechOid = GSSUtil.GSS_SPNEGO_MECH_OID;
					GSSManager manager = GSSManager.getInstance();
					GSSName serverName = manager.createName("HTTP/" + server, null);
					GSSContext context = manager.createContext(serverName, spnegoMechOid, null,
							GSSContext.DEFAULT_LIFETIME);
					context.requestMutualAuth(true);
					context.requestCredDeleg(true);
					byte[] token = new byte[0];
					token = context.initSecContext(token, 0, token.length);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestProperty("Authorization", "Negotiate " + Base64.encode(token));
					is = conn.getInputStream();
					negotialte = conn.getHeaderField("WWW-Authenticate");
					List<HttpCookie> cookies = ((CookieManager) (CookieManager.getDefault()))
							.getCookieStore().get(url.toURI());
					logger.info("Cookies:" + cookies);
					if (negotialte.startsWith("Negotiate")) {
						token = Base64.decode(negotialte.substring("Negotiate".length()).trim());
						try {
							context.initSecContext(token, 0, token.length);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (!context.isEstablished()) {
							logger.info("Context NOT established !!!");
						} else {
							logger.info("Context ESTABLISHED !!!");
						}
					}
				}
			} else
				throw e1;
		}
		StringBuffer content = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			content = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				content.append(inputLine);
			in.close();
			logger.info(content.toString());
			new GssTest(content.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	GssTest(final String content) {
		logger.info("Starting ...");
		logger.info(content.toString());
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		getContentPane().add(topPanel);
		JEditorPane pane = new JEditorPane();
		pane.setContentType("text/html");
		pane.setText(content.toString());
		JScrollPane scroll = new JScrollPane();
		scroll.getViewport().add(pane);
		topPanel.add(scroll, BorderLayout.CENTER);
		add(topPanel);
		setBounds(0, 0, 300, 300);
		scroll.setBounds(0, 0, 1000, 1000);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});
		setVisible(true);
	}
}
