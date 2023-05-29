package hellodocument;
// Licensed Materials - Property of IBM
//
// 5748-R81
//
// (C) Copyright IBM Corp. 2008 All Rights Reserved
//
// US Government Users Restricted Rights - Use, duplication or
// disclosure restricted by GSA ADP Schedule Contract with
// IBM Corp.

// This code is provided "as is" and is not supported by any IBM organization
// or individual.  It's intended to illustrate the use of some programming
// paradigms.  Those illustrations may or may not be the "best" way of doing
// certain things.  The code could also contain bugs.

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivilegedAction;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.DynamicReferentialContainmentRelationship;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.UpdatingBatch;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.util.UserContext;

/**
 * This is a "getting started" sample. It creates, populates, checks in, and
 * files a Document. Then it reads back the content of the Document and compares
 * it to the original. Nevertheless, it does illustrate some basic points about
 * programming with the CE 4.x Java API.
 * 
 * The class implements PrivilegedAction just so it can be used with a doAs().
 * Most of the "real business logic" is inside the run() method.
 */
public class HelloDocumentOrig implements PrivilegedAction {
	/**
	 * All of the configuration information for this sample is contained in this
	 * simple static inner class. The only reason for having this class is so
	 * all the config stuff appears in a single lump. In a real application,
	 * you'd probably have some other way of configuring things.
	 */
	private static final class ConfigInfo {
		/**
		 * The sample shows the use of a JAAS login and also the use of the API
		 * helpers for callers who still use the userid/password model. Which is
		 * which is controlled by this variable. In either case, you will need
		 * to have configured JAAS for your JVM.
		 */
		static boolean USE_EXPLICIT_JAAS_LOGIN = false;
		/**
		 * If you use the API helpers, you need a userid and password. To keep
		 * the sample simple, we've assumed the explicit JAAS login also needs
		 * them (via callbacks).
		 */
		static String USERID = "myuserid";
		/**
		 * See comments for USERID.
		 */
		static String PASSWORD = "mypassword";
		/**
		 * More JAAS configuration. Which stanza within our JAAS configuration
		 * should you use?
		 */
		static String JAAS_STANZA_NAME = "other";
		/**
		 * This URI tells us how to find the CE and what protocol to use.
		 */
		static String CE_URI = "http://myCEserver:7001/wsi/FNCEWS40DIME";
		/**
		 * This ObjectStore must already exist. It's where all the activity will
		 * happen.
		 */
		static String OBJECT_STORE_NAME = "MyObjectStore";
		/**
		 * Documents will be filed into this folder. If it doesn't exist, it
		 * will be created (directly under the root folder). Don't include any
		 * slashes in this folder name.
		 */
		static String FOLDER_NAME = "HelloDocument";
		/**
		 * This is the local file from which we will take content for the newly
		 * created repository document. There aren't any particular constraints
		 * on size or type.
		 */
		static String LOCAL_FILE_NAME = "c:/temp/SomeFile.foo";
		/**
		 * Completely unnecessary for the system, but people tend to like seeing
		 * this populated.
		 */
		static String DOCUMENT_TITLE = "My Document Title";
		/**
		 * We set this explicitly to illustrate that it's unrelated to the
		 * DocumentTitle as well as the original file name. When you are
		 * referencing a document by path, you use the containment name.
		 */
		static String CONTAINMENT_NAME = "I Am a Contained Document";
		/**
		 * You get your choice on the readback. You can either read and compare
		 * from the beginning or you can skip to a quasi-random point in the
		 * second half of the content and start there.
		 */
		static boolean USE_SKIP = true;
	}

	/**
	 * All interaction with the server will make use of this Connection object.
	 * Connections are actually stateless, so you don't have to worry about
	 * holding open some CE resource.
	 *
	 * no R/T
	 */
	private Connection conn = Factory.Connection.getConnection(ConfigInfo.CE_URI);

	/**
	 * The main() method just figures out how to authenticate and then directly
	 * or indirectly delegates to the run() method.
	 */
	public static void main(String[] args) throws LoginException {
		System.out.println("CE is at " + ConfigInfo.CE_URI);
		System.out.println("ObjectStore is " + ConfigInfo.OBJECT_STORE_NAME);
		HelloDocument fd = new HelloDocument();
		if (ConfigInfo.USE_EXPLICIT_JAAS_LOGIN) {
			loginAndRun(fd, ConfigInfo.USERID, ConfigInfo.PASSWORD);
		} else {
			// This is the standard Subject push/pop model for the helper
			// methods.
			Subject subject = UserContext.createSubject(fd.conn, ConfigInfo.USERID, ConfigInfo.PASSWORD,
					ConfigInfo.JAAS_STANZA_NAME);
			UserContext.get().pushSubject(subject);
			try {
				fd.run();
			} finally {
				UserContext.get().popSubject();
			}
		}
	}

	/**
	 * This method contains the actual business logic. Authentication has
	 * already happened by the time we get here.
	 */
	public Object run() {
		// Standard Connection -> Domain -> ObjectStore
		// no R/T
		Domain dom = Factory.Domain.getInstance(conn, null);
		// no R/T
		ObjectStore os = Factory.ObjectStore.getInstance(dom, ConfigInfo.OBJECT_STORE_NAME);

		String containmentName = createAndFileDocument(dom, os);

		File f = new File(ConfigInfo.LOCAL_FILE_NAME);
		long fileSize = f.length();
		System.out.println("Local content size is " + fileSize + " for file " + ConfigInfo.LOCAL_FILE_NAME);
		long skipPoint = 0L;
		if (ConfigInfo.USE_SKIP) {
			long midPoint = fileSize / 2;
			// pick a random point in the second half of the content
			skipPoint = midPoint + (long) Math.floor((Math.random() * midPoint));
		}
		System.out.println("Will skip to " + skipPoint + " of " + fileSize);

		readAndCompareContent(os, containmentName, skipPoint);
		return null;
	}

	/**
	 * As the name implies, this method actually creates a new document. It adds
	 * content from a local file, checks in the document, and files it in a
	 * folder.
	 * 
	 * Although this method could just as well return the Document object for
	 * subsequent use, we wanted to simulate coming back to things later and so
	 * will reconstruct the reference from the path. So, this method returns
	 * just the containment name actually used by the new document when filed.
	 */
	private String createAndFileDocument(Domain dom, ObjectStore os) {
		FileInputStream fis = openLocalFileForRead();

		// This is pretty standard for creating content for a brand new
		// Document. Since it is just being created, we know its ContentElements
		// property will be an empty list, so it's no harm to just replace it
		// with one cooked up from the Factory.
		//
		// no R/T
		ContentTransfer ct = Factory.ContentTransfer.createInstance();
		ct.setCaptureSource(fis);
		// optional
		ct.set_RetrievalName(ConfigInfo.LOCAL_FILE_NAME);
		// optional
		ct.set_ContentType("application/octet-stream");

		ContentElementList cel = Factory.ContentElement.createList();
		cel.add(ct);

		// no R/T
		Document doc = Factory.Document.createInstance(os, null);
		// not required
		doc.getProperties().putValue("DocumentTitle", ConfigInfo.DOCUMENT_TITLE);
		doc.set_ContentElements(cel);
		// no R/T
		doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);

		// The API provides the convenience method Folder.file(), but it's so
		// simple
		// to just create the RCR/DRCR directly that we do it that way here.
		Folder folder = instantiateFolder(os);
		// no R/T
		DynamicReferentialContainmentRelationship rcr = Factory.DynamicReferentialContainmentRelationship
				.createInstance(os, null, AutoUniqueName.AUTO_UNIQUE,
						DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rcr.set_Tail(folder);
		rcr.set_Head(doc);
		rcr.set_ContainmentName(ConfigInfo.CONTAINMENT_NAME);

		// To minimize the number of R/Ts, we save both the document and the RCR
		// in
		// a single batch. So, we've created a document, added content to it,
		// checked
		// it in, filed it in a folder, and it only cost a single R/T. Woo-hoo!
		//
		// (There was actually another R/T to check for the existence of the
		// folder. If we already knew we could depend on that, which would
		// normally
		// be the case in a real application, we wouldn't need that extra R/T.)
		//
		// ((OK, there are additional implicit R/Ts if the content being
		// uploaded exceeds
		// the configured chunk size for the API transport.))
		UpdatingBatch ub = UpdatingBatch.createUpdatingBatchInstance(dom, RefreshMode.REFRESH);
		ub.add(doc, null);
		ub.add(rcr, null);
		System.out.println("Doing updates via UpdatingBatch");
		ub.updateBatch();

		// We used refresh mode on the batch because we wanted to get back the
		// RCR containment name. Because we asked the server to make a unique
		// name (rather than throwing an exception on a collision), the actual
		// containment name could be different from the value we passed in.
		//
		// We could have used property filters to get back just that single
		// property on that single object, but we didn't want to clutter this
		// sample code with property filter stuff. PropertyFilters are very
		// valuable to optimizing performance.
		return rcr.get_ContainmentName();
	}

	/**
	 * The folder may or may not already exist. In a real application, you would
	 * probably know whether it existed or not. There are a couple of ways to
	 * handle this for the sample. (1) We could fetch the folder, falling back
	 * to creating it if that failed. (2) We could make a reference to the
	 * folder, do some operation on it, and fall back to creating the folder if
	 * the operation failed. (3) We can just try to create it and fall back to
	 * creating a fetchless reference to it if it already exists. We use (3)
	 * here. Our normal preference would be approach (2) since it doesn't cost
	 * any extra R/Ts, but the error handling would clutter up the logic when we
	 * try to file the document in the folder. For this sample, we'd rather have
	 * that logic be clear, so approach (3) it is.
	 */
	private Folder instantiateFolder(ObjectStore os) {
		Folder folder = null;
		try {
			// no R/T
			folder = Factory.Folder.createInstance(os, null);
			// no R/T
			Folder rootFolder = Factory.Folder.getInstance(os, null, "/");
			folder.set_Parent(rootFolder);
			folder.set_FolderName(ConfigInfo.FOLDER_NAME);
			// R/T
			folder.save(RefreshMode.NO_REFRESH);
		} catch (EngineRuntimeException ere) {
			// Create failed. See if it's because the folder exists.
			ExceptionCode code = ere.getExceptionCode();
			if (code != ExceptionCode.E_NOT_UNIQUE) {
				throw ere;
			}
			System.out.println("Folder already exists: /" + ConfigInfo.FOLDER_NAME);
			// no R/T
			folder = Factory.Folder.getInstance(os, null, "/" + ConfigInfo.FOLDER_NAME);
		}
		return folder;
	}

	/**
	 * Read the content from the first content element of a Document and compare
	 * it to the original local file content. Optionally, skip over some amount
	 * of both the local and remote content streams.
	 */
	private void readAndCompareContent(ObjectStore os, String containmentName, long skipPoint) {
		String fullPath = "/" + ConfigInfo.FOLDER_NAME + "/" + containmentName;
		System.out.println("Document: " + fullPath);
		// no R/T
		Document doc = Factory.Document.getInstance(os, null, fullPath);
		// R/T
		doc.refresh(new String[] { PropertyNames.CONTENT_ELEMENTS });
		InputStream str = doc.accessContentStream(0);
		try {
			FileInputStream fis = openLocalFileForRead();
			if (skipPoint < 0)
				skipPoint = 0;
			if (skipPoint > 0) {
				fis.skip(skipPoint);
				str.skip(skipPoint);
			}

			System.out.print("Starting content comparison");
			for (long position = skipPoint;; ++position) {
				if (position % 1000 == 0)
					System.out.print('.');
				if (position % 100000 == 0)
					System.out.println();
				// It's pretty expensive to do this single-byte read() calls,
				// but
				// it makes the code clearer for the sample. In a real
				// application,
				// you would probably use one of the read(byte[]) signatures.
				int chRemote = str.read();
				int chLocal = fis.read();
				if (chRemote != chLocal) {
					String local = (chLocal == -1 ? "EOF" : "0x" + Integer.toHexString(chLocal));
					String remote = (chRemote == -1 ? "EOF" : "0x" + Integer.toHexString(chRemote));
					throw new RuntimeException("Local and remote content differs at position " + position + "; local "
							+ local + ", remote " + remote);
				}
				if (chRemote == -1) {
					System.out.println("\nLocal and remote content is identical from position " + skipPoint);
					break;
				}
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Helper method
	 */
	private FileInputStream openLocalFileForRead() {
		try {
			return new FileInputStream(ConfigInfo.LOCAL_FILE_NAME);
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		}
	}

	/**
	 * Helper method only used if you are doing explicit JAAS login. In fact,
	 * here is where that explicit login happens.
	 * 
	 * Unfortunately, doAs() is an area where not all appservers behave the same
	 * way. This form will always work if you are using WS transport for the
	 * Connection. If you are using EJB transport, you should consult your
	 * appserver product documentation to see how they want you to accomplish a
	 * doAs().
	 * 
	 * The good news is that if you were running as a web app, an EJB, or some
	 * other J2EE component, it is most likely that authentication will already
	 * be handled somehow before it gets to your application.
	 */
	private static final void loginAndRun(HelloDocument fd, String userid, String password) throws LoginException {
		LoginContext lc = new LoginContext(ConfigInfo.JAAS_STANZA_NAME, new HelloDocument.CallbackHandler(userid,
				password));
		lc.login();
		Subject subject = lc.getSubject();
		Subject.doAs(subject, fd);
	}

	/**
	 * This is standard JAAS userid/password callback handling. It's only used
	 * if you are doing explicit JAAS login. If you want to know how this works,
	 * consult standard JAAS documentation.
	 */
	private static final class CallbackHandler implements javax.security.auth.callback.CallbackHandler {
		private String userid;
		private String password;

		public CallbackHandler(String userid, String password) {
			this.userid = userid;
			this.password = password;
		}

		public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
			for (int i = 0; i < callbacks.length; i++) {
				if (callbacks[i] instanceof TextOutputCallback) {
					// TextOutputCallback toc =
					// (TextOutputCallback)callbacks[i];
					// System.err.println("JAAS callback: " + toc.getMessage());
				} else if (callbacks[i] instanceof NameCallback) {
					NameCallback nc = (NameCallback) callbacks[i];
					nc.setName(userid);
				} else if (callbacks[i] instanceof PasswordCallback) {
					PasswordCallback pc = (PasswordCallback) callbacks[i];
					pc.setPassword(password.toCharArray());
				} else {
					throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
				}
			}
		}
	}
}
