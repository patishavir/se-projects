package oz.hello;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HelloAIX {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		StringBuilder sb = new StringBuilder();
		sb.append(calendar.toString());
		sb.append("\n");
		sb.append(System.getProperty("user.timezone"));
		sb.append("\n");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		sb.append(sdf.format(calendar.getTime()));
		sb.append("\nHello AIX !!!!????");
		System.out.println(sb.toString());

	}
}
