package oz.infra.console;

import java.util.Scanner;

public class ConsoleUtils {

	public static String readLineFromConsole() {
		System.out.println("Enter something here : ");
		Scanner scanIn = new Scanner(System.in);
		String line = scanIn.nextLine();
		scanIn.close();
		System.out.println(line);
		return line;
	}

}
