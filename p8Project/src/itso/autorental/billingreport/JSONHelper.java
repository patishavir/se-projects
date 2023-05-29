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
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class JSONHelper {
	
	
	public static void serialize(List list, Writer out) throws IOException {
		out.write("[");
		boolean isFirst = true;
		Iterator iterator = list.iterator();
		
		while (iterator.hasNext()) {
			Object object = iterator.next();
			if (isFirst) {
				isFirst = false;
			} else {
				out.write(",");
			}
			serialize(object, out);
		}
		out.write("]");
	}
	
	public static void serialize(Map map, Writer out) throws IOException {
		out.write("{");
		boolean isFirst = true;
		
		Set entrySet = map.entrySet();
		
		Iterator iterator = entrySet.iterator();
		
		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			if (isFirst) {
				isFirst = false;
			} else {
				out.write(", ");
			}
			out.write("\"");
			out.write(entry.getKey().toString());
			out.write("\":");
			Object value = entry.getValue();
			serialize(value, out);
		}
		out.write("}");
		
	}
	
	public static void serialize(Object value, Writer out) throws IOException {
		if (value instanceof List) {
			List list = (List) value;
			serialize(list, out);
		} else if (value instanceof Map) {
			Map map = (Map) value;
			serialize(map, out);
		} else {
			out.write("\"");
			out.write(value == null ? "" : value.toString());
			out.write("\"");
		}
	}
}
