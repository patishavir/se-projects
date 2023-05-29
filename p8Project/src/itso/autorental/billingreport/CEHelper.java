/*
 * DISCLAIMER OF WARRANTIES.  The following [enclosed] code is
 * sample code created by IBM Corporation.  This sample code is
 * not part of any standard or IBM product and is provided to you
 * solely for the purpose of assisting you in the development of
 * your applications.  The code is provided "AS IS", without
 * warranty of any kind.  IBM shall not be liable for any damages
 * arising out of your use of the sample code, even if they have
 * been advised of the possibility of such damages.
 */

package itso.autorental.billingreport;

import java.io.IOException;
import javax.security.auth.Subject;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext; 

public class CEHelper {
	private String jaasStanza = "";
	private String ceUri = "";
	private String userName = "";
	private String password = "";
	private String defaultObjectStoreName = "";
	
	private Connection connection = null;
	
	public void init() throws IOException {
		jaasStanza = System.getProperty("jaas.stanza", "FileNetP8WSI");
		ceUri = System.getProperty("ce.uri", "http://localhost:9080/wsi/FNCEWS40MTOM");
		defaultObjectStoreName = System.getProperty("default.objectstore.name", "COLL");
	}
	
	public void validateConnection() {
		Factory.User.fetchCurrent(connection, null);
	} 
	
	public Connection getConnection() {
		if (connection == null) {
			UserContext userContext = UserContext.get();
			connection = Factory.Connection.getConnection(this.ceUri);
			// The Subjects pushed to UserContext are stored thread-local, so this
			// Subject does not "stick" to this connection. You need to balance the
			// pushes and pops for a thread. This is important for two reasons in a
			// thread-pool environment:
			// 1. If you do push over and over again with no pops, you are stacking
			//  up many Subjects in thread-local storage.  With no pops (or with fewer
			//  pops than pushes), this is a memory leak.
			// 2. If another application using the same thread pool can end up using one
			//  of the pushed Subjects left over on the thread from the first 
			//  application.  This can happen through a bug in the second application 
			//  (it doesn't push when it should) or when the second application thinks 
			//  it will use the ambient JAAS Subject from container-managed 
			//  authentication. 
			Subject subject = UserContext.createSubject(connection,
					this.userName, this.password, this.jaasStanza);
			userContext.pushSubject(subject);
		}
		
		validateConnection();
		return connection;
	}
	
	public void login(String userName, String password) {
		this.userName = userName;
		this.password = password;
		this.connection = null;
		getConnection();
	}

	public ObjectStore getObjectStore(Connection connection,
			String objectStoreName) {
		Domain domain = Factory.Domain.getInstance(connection, null);
		ObjectStore os = Factory.ObjectStore.fetchInstance(domain, objectStoreName, null);
		return os;
	}
	
	public ObjectStore getDefaultObjectStore(Connection connection) {
		Domain domain = Factory.Domain.getInstance(connection, null);
		ObjectStore os = Factory.ObjectStore.fetchInstance(domain, defaultObjectStoreName, null);
		return os;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
