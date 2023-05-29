package oz.utils.fileconversion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import oz.infra.string.StringUtils;

public class ListRecordsThatHaveNulls {
	private final static char NULL_CHARACTER = Character.MIN_VALUE;
	private static char[] nullCharArray = { NULL_CHARACTER };
	private final static String NULL_STRING = new String(nullCharArray);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String inputFilePath = args[0];
		File inFile = new File(inputFilePath);

		BufferedReader input = null;
		StringBuilder exceptionsStringBuilder = new StringBuilder();
		int nullCharctersRecordsFound = 0;
		int nullCharctersFound = 0;
		try {
			input = new BufferedReader(new FileReader(inFile));
			String inFileString = null; // not declared within while loop
			int nullCharacterIndex = 0;

			while ((inFileString = input.readLine()) != null) {
				nullCharacterIndex = inFileString.indexOf(NULL_CHARACTER);
				if (nullCharacterIndex > -1) {
					nullCharctersRecordsFound++;
					exceptionsStringBuilder.append(inFileString + "\n");
					nullCharctersFound = nullCharctersFound
							+ StringUtils.getNumberOfOccurrences(inFileString, NULL_CHARACTER);

				}
			}

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (input != null) {
					// flush and close both "input" and its underlying
					// FileReader
					input.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println(exceptionsStringBuilder.toString());
		System.out.println(exceptionsStringBuilder.toString().replaceAll(NULL_STRING, " "));
		System.out.println("Found " + nullCharctersRecordsFound + " records with null characters");
		System.out.println("Found " + nullCharctersFound + " null characters");
	}

}
