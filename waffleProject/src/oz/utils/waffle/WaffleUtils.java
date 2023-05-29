package oz.utils.waffle;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Sspi;
import com.sun.jna.platform.win32.Sspi.SecBufferDesc;

import waffle.windows.auth.IWindowsAccount;
import waffle.windows.auth.IWindowsAuthProvider;
import waffle.windows.auth.IWindowsComputer;
import waffle.windows.auth.IWindowsCredentialsHandle;
import waffle.windows.auth.IWindowsDomain;
import waffle.windows.auth.IWindowsIdentity;
import waffle.windows.auth.IWindowsSecurityContext;
import waffle.windows.auth.impl.WindowsAuthProviderImpl;
import waffle.windows.auth.impl.WindowsCredentialsHandleImpl;
import waffle.windows.auth.impl.WindowsSecurityContextImpl;

public class WaffleUtils {
	public static void getGroups(final String userName, final String password) {
		IWindowsAuthProvider prov = new WindowsAuthProviderImpl();
		IWindowsIdentity identity = prov.logonUser(userName, password);
		System.out.println("User identity: " + identity.getFqn());
		for (IWindowsAccount group : identity.getGroups()) {
			System.out.println(" " + group.getFqn() + " (" + group.getSidString() + ")");
		}
	}

	public static void getDomains() {
		IWindowsAuthProvider prov = new WindowsAuthProviderImpl();
		IWindowsDomain[] domains = prov.getDomains();
		for (IWindowsDomain domain : domains) {
			System.out.println(domain.getFqn() + ": " + domain.getTrustDirectionString());

		}
	}

	public static void isComputerInDonain() {
		IWindowsAuthProvider prov = new WindowsAuthProviderImpl();
		IWindowsComputer computer = prov.getCurrentComputer();
		System.out.println(computer.getComputerName());
		System.out.println(computer.getJoinStatus());
		System.out.println(computer.getMemberOf());
	}

	public static void getLocalGroups() {
		IWindowsAuthProvider prov = new WindowsAuthProviderImpl();
		IWindowsComputer computer = prov.getCurrentComputer();
		String[] localGroups = computer.getGroups();
		for (String localGroup : localGroups) {
			System.out.println(" " + localGroup);
		}
	}

//	public static void yyy() {
//		// client credentials handle
//		IWindowsCredentialsHandle credentials = WindowsCredentialsHandleImpl.getCurrent("Negotiate");
//		credentials.initialize();
//		// initial client security context
//		WindowsSecurityContextImpl clientContext = new WindowsSecurityContextImpl();
//		clientContext.setPrincipalName(Advapi32Util.getUserName());
//		clientContext.setCredentialsHandle(credentials.getHandle());
//		clientContext.setSecurityPackage(securityPackage);
//		clientContext.initialize();
//		// accept on the server
//		WindowsAuthProviderImpl provider = new WindowsAuthProviderImpl();
//		IWindowsSecurityContext serverContext = null;
//		do {
//			if (serverContext != null) {
//				// initialize on the client
//				SecBufferDesc continueToken = new SecBufferDesc(Sspi.SECBUFFER_TOKEN, serverContext.getToken());
//				clientContext.initialize(clientContext.getHandle(), continueToken);
//			}
//			// accept the token on the server
//			serverContext = provider.acceptSecurityToken(clientContext.getToken(), "Negotiate");
//		} while (clientContext.getContinue() || serverContext.getContinue());
//		System.out.println(serverContext.getIdentity().getFqn());
//		for (IWindowsAccount group : serverContext.getIdentity().getGroups()) {
//			System.out.println(" " + group.getFqn());
//		}
//	}
}
