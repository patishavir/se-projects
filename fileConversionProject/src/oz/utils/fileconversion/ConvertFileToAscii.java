package oz.utils.fileconversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class ConvertFileToAscii {

	public static void main(String[] args) {

		String inputFileName = null;
		String outputFileName = null;
		int recordlength = 0;

		if (args.length < 3) {
			System.out.println("");
			System.out.println("ConvertFileToAscii - convert file from Ebcdic to Ascii");
			System.out.println("");
			System.out.println("Usage: ConvertFileToAscii inputfile outputfile recordlength");
			System.out.println("");
			System.out.println(" note: codepage input IBM-424 codepage output WINDOWS-1255");
			return;
		}

		inputFileName = args[0];
		outputFileName = args[1];
		try {
			recordlength = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			System.out.println("Invalid record length '" + args[2] + "'.");
			return;
		}

		try {
			ConvertFileToAscii convertFileToAscii = new ConvertFileToAscii();
			convertFileToAscii.convert(inputFileName, outputFileName, recordlength);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			return;
		}

		System.out.println("Done.");
	}

	public void convert(String inputFileName, String outputFileName, int recordlength)
			throws Exception {
		// Initalize Input file
		File fileInput = new File(inputFileName);
		if (fileInput == null || !fileInput.isFile() || !fileInput.exists() || !fileInput.canRead()) {
			throw new Exception("Can't read file '" + inputFileName + "'.");
		}

		// Initalize Output file
		File fileOutput = new File(outputFileName);
		if (fileOutput != null) {
			if (!fileOutput.exists()) {
				try {
					fileOutput.createNewFile();
				} catch (IOException exception) {
					throw new Exception("Can't write to file '" + outputFileName + "'.\n"
							+ exception.getMessage());
				}
			}
		}
		if (fileOutput == null || !fileOutput.isFile() || !fileOutput.exists()
				|| !fileOutput.canWrite()) {
			throw new Exception("Can't write to file '" + outputFileName + "'.");
		}

		// open the files
		BufferedReader inputReader = null;
		BufferedWriter outputWriter = null;
		try {
			inputReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileInput),
					"IBM-424"));
			outputWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
					fileOutput), "WINDOWS-1255"));
		} catch (UnsupportedEncodingException exception) {
			try {
				if (inputReader != null)
					inputReader.close();
				if (outputWriter != null)
					outputWriter.close();
				if (fileOutput != null)
					fileOutput.delete();
			} catch (IOException ex) {
			}
			throw exception;
		} catch (FileNotFoundException exception) {
			try {
				if (inputReader != null)
					inputReader.close();
				if (outputWriter != null)
					outputWriter.close();
				if (fileOutput != null)
					fileOutput.delete();
			} catch (IOException ex) {
			}
			throw exception;
		}

		// check record length
		if ((fileInput.length() % recordlength) != 0) {
			try {
				if (inputReader != null)
					inputReader.close();
				if (outputWriter != null)
					outputWriter.close();
				if (fileOutput != null)
					fileOutput.delete();
			} catch (IOException ex) {
			}
			throw new Exception("Invalid record length '" + recordlength
					+ "'.\nFile input length is " + fileInput.length() + " byts.");
		}

		// Convert the file
		try {
			int charectersRead = 0;
			char[] buffer = new char[recordlength];
			while (inputReader.ready()
					&& (charectersRead = inputReader.read(buffer, 0, recordlength)) != -1) {
				outputWriter.write(buffer, 0, charectersRead);
				outputWriter.write(System.getProperty("line.separator"));
			}
		} catch (IOException exception) {
			try {
				if (inputReader != null)
					inputReader.close();
				if (outputWriter != null)
					outputWriter.close();
				if (fileOutput != null)
					fileOutput.delete();
			} catch (IOException ex) {
			}
			throw (exception);
		}

		// Close the files.
		try {
			if (inputReader != null) {
				inputReader.close();
			}

			if (outputWriter != null) {
				outputWriter.flush();
				outputWriter.close();
			}
		} catch (IOException ex) {
		}
	}
}
