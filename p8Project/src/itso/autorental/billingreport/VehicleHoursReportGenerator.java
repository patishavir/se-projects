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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectReference;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyEngineObject;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;

public class VehicleHoursReportGenerator {
	
	private static Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
	
	private static final String ITSO_IDLE_ACTIVITY = "ITSOIdleActivity";
	private static final String ITSO_RENTAL_ACTIVITY = "ITSORentalActivity";
	private static final String ITSO_MAINTENANCE_ACTIVITY = "ITSOMaintenanceActivity";
	
	private static float MILLSECS_PER_HOUR = 1000*60*60;
	
	
	private static final ThreadLocal QUERYFORMAT_THREAD_LOCAL = 
        new ThreadLocal() {
			protected Object initialValue() {
				SimpleDateFormat queryFormat = new SimpleDateFormat("yyyyMMdd'T'hhmmss'Z'");
				queryFormat.setCalendar(cal);
				return queryFormat;
			}
		};
		
	private static final ThreadLocal PRINTFORMAT_THREAD_LOCAL = 
	    new ThreadLocal() {
			protected Object initialValue() {
				SimpleDateFormat printFormat = new SimpleDateFormat("yyyy-MM-dd");
				printFormat.setCalendar(cal);
				return printFormat;
			}
		};
		
	private static final ThreadLocal DECIMALFORMAT_THREAD_LOCAL = 
		new ThreadLocal() {
			protected Object initialValue() {
				DecimalFormat   nf   =   new   DecimalFormat(".00");
				return nf;
			}
		};

	public static String formatQueryDate(Date date) {
		return ((SimpleDateFormat)QUERYFORMAT_THREAD_LOCAL.get()).format(date);
	}
	
	public static String formatPrintDate(Date date) {
		return ((SimpleDateFormat)PRINTFORMAT_THREAD_LOCAL.get()).format(date);
	}
	
	public static String formatFloat(Float number) {
		return ((DecimalFormat)DECIMALFORMAT_THREAD_LOCAL.get()).format(number);
	}

	static void queryVehicleActivities(Date start, Date end, ObjectStore os, Map result) {
		String sqlStatement = 	
			"select ITSOStartDate, ITSOEndDate, ITSOVehicle, ITSOFranchiseCode, This "+
			"from ITSOVehicleActivity " + 
			"where " + 
				"ITSOStartDate > " + formatQueryDate(start) + " AND " +
				"ITSOEndDate < " + formatQueryDate(end);
		
		System.out.println("SQL:" + sqlStatement);
		SearchSQL sql = new SearchSQL(sqlStatement);

		SearchScope scope = new SearchScope(os);
		IndependentObjectSet set = scope.fetchObjects(sql, null, null, true);
		Iterator it = set.iterator();
		
		List idleActivities = new ArrayList();
		List rentalActivities = new ArrayList();
		List maintenanceActivities = new ArrayList();
		
		int numberOfIdles = 0;
		int numberOfMaintenances = 0;
		int numberOfRentals = 0;
		float totalHoursForIdles = 0;
		float totalHoursForMaintenances = 0;
		float totalHoursForRentals = 0;
		while(it.hasNext()) {
			Map activityJSON = new HashMap();
			CustomObject idleActivity = (CustomObject)it.next();
			Properties properties = idleActivity.getProperties();
			Date startDate = properties.getDateTimeValue("ITSOStartDate");
			Date endDate = properties.getDateTimeValue("ITSOEndDate");
			String franchiseCode = properties.getStringValue("ITSOFranchiseCode");
			Folder vehicleFolder = (Folder) properties.getEngineObjectValue("ITSOVehicle");
			
			PropertyEngineObject peo = (PropertyEngineObject)properties.get("This");
		    ObjectReference object = peo.getObjectReference();
	        
	        String className  = object.getClassIdentity();
	        System.out.println("ClassName:" + className);
	        
	        float difference = endDate.getTime() - startDate.getTime();
			float hours = (difference/MILLSECS_PER_HOUR);
	        
			String vehicleID = vehicleFolder.getProperties().getStringValue("ITSOVehicleId");
			activityJSON.put("startDate", formatPrintDate(startDate));
			activityJSON.put("endDate", formatPrintDate(endDate));
			activityJSON.put("franchiseCode", franchiseCode);
			activityJSON.put("vehicleID", vehicleID);
			activityJSON.put("hours", formatFloat(hours));
	        
	        if (className.equals(ITSO_MAINTENANCE_ACTIVITY)) {
	        	numberOfMaintenances++;
				totalHoursForMaintenances += hours;
				maintenanceActivities.add(activityJSON);
	        } else if (className.equals(ITSO_RENTAL_ACTIVITY)) {
	        	numberOfRentals++;
				totalHoursForRentals += hours;
				rentalActivities.add(activityJSON);
	        } else if (className.equals(ITSO_IDLE_ACTIVITY)) {
	        	numberOfIdles++;
				totalHoursForIdles += hours;
				idleActivities.add(activityJSON);
	        }
		}
		
		System.out.println("numberOfMaintenances:" + numberOfMaintenances);
		System.out.println("numberOfRentals:" + numberOfRentals);
		System.out.println("numberOfIdles:" + numberOfIdles);
		
		result.put("startDate", formatPrintDate(start));
		result.put("endDate", formatPrintDate(end));
		
		result.put("numberOfMaintenances", numberOfMaintenances);
		result.put("numberOfRentals", numberOfRentals);
		result.put("numberOfIdles", numberOfIdles);
		
		result.put("totalHoursForMaintenances",formatFloat(totalHoursForMaintenances));
		result.put("totalHoursForRentals", formatFloat(totalHoursForRentals));
		result.put("totalHoursForIdles", formatFloat(totalHoursForIdles));
		
		result.put("maintenances", maintenanceActivities);
		result.put("rentals", rentalActivities);
		result.put("idles", idleActivities);
		
		System.out.println(result.toString());
	}
	
	public static Map queryVehicleHoursSummary(Date start, Date end, ObjectStore os) {
		Map result = new HashMap();
		queryVehicleActivities(start, end, os, result);
		System.out.println(result.toString());
		return result;
	}
}
