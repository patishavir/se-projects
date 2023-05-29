package oz.utils.waffle;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Sspi;
import com.sun.jna.platform.win32.Sspi.SecBufferDesc;

import waffle.windows.auth.IWindowsAccount;
import waffle.windows.auth.IWindowsCredentialsHandle;
import waffle.windows.auth.IWindowsSecurityContext;
import waffle.windows.auth.impl.WindowsAuthProviderImpl;
import waffle.windows.auth.impl.WindowsCredentialsHandleImpl;
import waffle.windows.auth.impl.WindowsSecurityContextImpl;

public class NegotiateSso {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String securityPackage = "Negotiate";
		// client credentials handle
		IWindowsCredentialsHandle clientCredentials = WindowsCredentialsHandleImpl.getCurrent(securityPackage);
		clientCredentials.initialize();
		// initial client security context
		WindowsSecurityContextImpl clientContext = new WindowsSecurityContextImpl();
		clientContext.setPrincipalName(Advapi32Util.getUserName());
		clientContext.setCredentialsHandle(clientCredentials.getHandle());
		clientContext.setSecurityPackage(securityPackage);
		clientContext.initialize();
		// accept on the server
		WindowsAuthProviderImpl provider = new WindowsAuthProviderImpl();
		IWindowsSecurityContext serverContext = null;
		do
		{
		if (serverContext != null) {
		// initialize on the client
		SecBufferDesc continueToken = new SecBufferDesc(Sspi.SECBUFFER_TOKEN, serverContext.getToken());
		clientContext.initialize(clientContext.getHandle(), continueToken);
		}
		// accept the token on the server
		serverContext = provider.acceptSecurityToken(clientContext.getToken(), securityPackage);
		} while (clientContext.getContinue() || serverContext.getContinue());
		System.out.println(serverContext.getIdentity().getFqn());
		for (IWindowsAccount group : serverContext.getIdentity().getGroups()) {
		System.out.println(” ” + group.getFqn());
		}
		serverContext.dispose();
		clientContext.dispose();
		clientCredentials.dispose();


	}

}
