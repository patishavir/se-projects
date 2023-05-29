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
import java.util.Arrays;

public class ConvertFileToEbcdic {

	public static void main(String[] args) {

		String inputFileName = null;
		String outputFileName = null;
		int recordlength = 0;

		if (args.length < 3) {
			System.out.println("");
			System.out.println("ConvertFileToEbcdic - convert file from Ascii to Ebcdic");
			System.out.println("");
			System.out.println("Usage: ConvertFileToEbcdic inputfile outputfile recordlength");
			System.out.println("");
			System.out.println(" note: codepage input WINDOWS-1255 codepage output IBM-424");
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
			ConvertFileToEbcdic convertFileToEbcdic = new ConvertFileToEbcdic();
			convertFileToEbcdic.convert(inputFileName, outputFileName, recordlength);
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			return;
		}

		System.out.println("Done.");
	}

	public void convert(String inputFileName, String outputFileName, int recordlength)
			throws Exception {
		convert(inputFileName, outputFileName, recordlength, false);
	}

	public void convert(String inputFileName, String outputFileName, int recordlength,
			boolean acceptTruncated) throws Exception {
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
					"WINDOWS-1255"));
			outputWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
					fileOutput), "IBM-424"));
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

		// Convert the file
		try {
			int charectersRead = 0;
			char[] buffer = new char[recordlength];
			String line = null;
			int lineLength = 0;
			while ((line = inputReader.readLine()) != null) {

				// check record length
				lineLength = line.length();

				if (lineLength > recordlength) {
					if (acceptTruncated) {
						lineLength = recordlength;
					} else {
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
								+ "'.\nFile input have record length '" + lineLength
								+ "'.\nTruncation is not allowed");
					}
				}

				// copy array
				Arrays.fill(buffer, ' ');
				System.arraycopy(line.toCharArray(), 0, buffer, 0, lineLength);
				outputWriter.write(buffer, 0, recordlength);
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
