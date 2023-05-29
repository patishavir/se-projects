package mataf.p8.servlet;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.security.auth.Subject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.UpdatingBatch;
import com.filenet.api.property.Properties;
import com.filenet.api.util.UserContext;

/**
 * Servlet implementation class P8
 */
public class Insert extends HttpServlet {
	private static final long serialVersionUID = 1L;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Insert() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		

		String uri = "iiop://s5381076.fibi.corp:2809/FileNet/Engine";
		String user = "_Svc4S5381130#WSSNFT";
		String password = "9f4tBwXiMS21";
		String stanza = "FileNetP8Server";
		String osName = "BBL_SNIFIT";
		String className = "MTF_MAAREHET_SNIFIT_DOC";

		UserContext uc = null;
		ObjectStore os = null;
		
		response.setContentType("text/plain");
		ServletOutputStream out = response.getOutputStream();
		try {
			
			

			Connection con = Factory.Connection.getConnection(uri);
			
			Subject sub = UserContext.createSubject(con, user, password, stanza);
            uc = UserContext.get();
			uc.pushSubject(sub);
			Domain dom = Factory.Domain.getInstance(con, null);
			os = Factory.ObjectStore.getInstance(dom,osName);
			
			
			ContentTransfer ct = Factory.ContentTransfer.createInstance();
			ct.setCaptureSource(new FileInputStream("/Mataf/Temp/pdf.pdf"));
			ct.set_ContentType("application/pdf");
			ct.set_RetrievalName("Test");
			
			ContentElementList cel =Factory.ContentElement.createList();
			cel.add(ct);
			
			Document doc = Factory.Document.createInstance(os,className);
			
			doc.set_ContentElements(cel);
			Properties prp = doc.getProperties();
			Date d = new Date();
			prp.putValue("DocumentTitle", "Test-"+sdf.format(d));
			prp.putValue("Maarehet", 19);
			prp.putValue("ShemTahana","STATION4");
			//prp.putValue("TaarihShmira",sdf.parse("02/06/2015 07:15:24.596"));
			prp.putValue("TaarihShmira",new Date());
			prp.putValue("Pakid","999999");
			prp.putValue("Heshbon",100633);
			prp.putValue("Bank",31);
			prp.putValue("Snif",284);
			prp.putValue("ZihuiPeula","LoadTest");
			prp.putValue("SiduriTahana",0);
			prp.putValue("TeurMismah","Load Test");
			//prp.putValue("TaarihAsakim",sdf.parse("01/01/2015 00:00:00.000"));
			prp.putValue("TaarihAsakim",new Date());
			
			
			
			doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);

			out.println("checkin complete");
			
			// Online Mode
			//doc.save(RefreshMode.REFRESH);
			
			// Batch Mode
			UpdatingBatch ub = UpdatingBatch.createUpdatingBatchInstance(dom, RefreshMode.REFRESH);
			ub.add(doc,null);
			ub.updateBatch();
				
			out.println("save complete");
			
			
		} catch (Exception ex) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ex.printStackTrace(new PrintStream(baos));
			out.print(new String(baos.toByteArray()));

		} finally {
			if (uc != null) {
				uc.popSubject();
			}
		}
	}
	
}
