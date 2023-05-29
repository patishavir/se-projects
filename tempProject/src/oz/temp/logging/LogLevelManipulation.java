package oz.temp.logging;

import java.util.logging.Level;

public class LogLevelManipulation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String levelString = "FINE";
		Level myLevel = Level.parse(levelString);
		System.out.print(myLevel.toString());
	}
}