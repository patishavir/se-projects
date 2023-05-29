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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.filenet.api.core.Connection;
import com.filenet.api.core.ObjectStore;


public class ReportApplication {
	CEHelper ceHelper;
	void generateReport(Date startDate, Date endDate) throws IOException {
		Connection connection = ceHelper.getConnection();
		ObjectStore defaultObjectStore = ceHelper.getDefaultObjectStore(connection);
		Map reportData = VehicleHoursReportGenerator.queryVehicleHoursSummary(startDate, endDate, defaultObjectStore);
		
		FileWriter fWriter = new FileWriter(System.getProperty("user.dir") + "/" + "activities.js");
		BufferedWriter out = new BufferedWriter(fWriter);
        out.write("var data = ");
		JSONHelper.serialize(reportData, out);
        out.write(";");
		out.close();
	}
	
	public void init() throws IOException {
		ceHelper = new CEHelper();
		ceHelper.init();
		ReportMainWindow mainWindow = new ReportMainWindow(this);
		mainWindow.open();
	}
	
	public static void main(String [] args) throws Exception {
		ReportApplication application = new ReportApplication();
		application.init();
	}

	public CEHelper getCEHelper() {
		return ceHelper;
	}
}
